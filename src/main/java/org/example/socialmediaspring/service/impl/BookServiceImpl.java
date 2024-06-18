package org.example.socialmediaspring.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.socialmediaspring.common.IsbnGenerator;
import org.example.socialmediaspring.common.PageNewResponse;
import org.example.socialmediaspring.common.PageResponse;
import org.example.socialmediaspring.constant.Common;
import org.example.socialmediaspring.constant.ErrorCodeConst;
import org.example.socialmediaspring.dto.book.BookCategoryDto;
import org.example.socialmediaspring.dto.book.SearchBookRequest;
import org.example.socialmediaspring.dto.common.IdsRequest;
import org.example.socialmediaspring.dto.book.BookRequest;
import org.example.socialmediaspring.dto.book.BookResponse;
import org.example.socialmediaspring.entity.Book;
import org.example.socialmediaspring.entity.BookCategory;
import org.example.socialmediaspring.entity.Category;
import org.example.socialmediaspring.exception.BizException;
import org.example.socialmediaspring.mapper.BookMapper;
import org.example.socialmediaspring.repository.*;
import org.example.socialmediaspring.service.BookService;
import org.example.socialmediaspring.utils.JsonUtils;
import org.springframework.data.domain.*;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookServiceImpl implements BookService {
    private final BookMapper bookMapper;

    private final BookRepository bookRepository;

    private  final EntityManager entityManager;

    private final IsbnGenerator isbnGenerator;

    private final CategoryRepository categoryRepository;

    private final BookCategoryRepository bookCategoryRepository;

    private final BookCustomRepositoryImpl bookCustomRepositoryImpl;


    private static final int BATCH_SIZE = 1000;

    @Override
    public BookCategoryDto saveBook(BookRequest bookRequest) {

        if (bookRepository.existsByTitle(bookRequest.getTitle())) {
            throw new BizException(ErrorCodeConst.INVALID_INPUT, "Book title existed");
        }

        // check list category ids exists db
        for (Integer categoryId : bookRequest.getCateIds()) {
            Optional<Category> category = categoryRepository.findById(categoryId);
            if (category.isEmpty()) {
                throw new BizException(ErrorCodeConst.VALIDATE_VIOLATION, "Category id " + categoryId + " does not exist.");
            }
        }

        Book book = bookMapper.toBook(bookRequest);

        book.setIsbn(isbnGenerator.generateISBN());
        book.setQuantityAvail(book.getQuantity());

        Book savedBook =  bookRepository.save(book);

        // save book_categories record
        List<BookCategory> bookCategoriesEntity = new ArrayList<>();

        for (Integer categoryId : bookRequest.getCateIds()) {
            BookCategory bookCategory = new BookCategory();
            bookCategory.setBookId(savedBook.getId());
            bookCategory.setCategoryId(categoryId);

            bookCategoriesEntity.add(bookCategory);
        }
        bookCategoryRepository.saveAll(bookCategoriesEntity);

        return BookCategoryDto.builder()
                .id(savedBook.getId())
                .cateIds(bookRequest.getCateIds())
                .title(savedBook.getTitle())
                .author(savedBook.getAuthor())
                .isbn(savedBook.getIsbn())
                .price(savedBook.getPrice())
                .yearOfPublish(savedBook.getYearOfPublish())
                .quantity(savedBook.getQuantity())
                .quantityAvail(savedBook.getQuantityAvail())
                .created(savedBook.getCreated())
                .modified(savedBook.getModified())
                .build();
    }
    @Override
    public BookCategoryDto updateBook(Integer id, BookRequest request) {
        Book existsBook = bookRepository.findById(id)
                .orElseThrow(() -> new BizException(ErrorCodeConst.INVALID_INPUT, "Book not found with id " + id));

        // check another title same exists
        bookRepository.findBookByTitle(request.getTitle()).ifPresent(book -> {
            if (!book.getId().equals(id)) {
                throw new BizException(ErrorCodeConst.INVALID_INPUT,"Book with title " + request.getTitle() + " already exists");
            }
        });

        // check list category ids exists db
        for (Integer categoryId : request.getCateIds()) {
            Optional<Category> category = categoryRepository.findById(categoryId);
            if (category.isEmpty()) {
                throw new BizException(ErrorCodeConst.VALIDATE_VIOLATION, "Category id " + categoryId + " does not exist.");
            }
        }

        existsBook.setTitle(request.getTitle());
        existsBook.setAuthor(request.getAuthor());
        existsBook.setPrice(request.getPrice());
        existsBook.setDescription(request.getDescription());
        existsBook.setQuantity(request.getQuantity());
        existsBook.setYearOfPublish(request.getYear());

        Book savedBook =  bookRepository.save(existsBook);

        // delete and create new book_categories
        List<BookCategory> bookCategoriesEntity = new ArrayList<>();

        bookCategoryRepository.deleteBookCateByBookId(id);

        for (Integer categoryId : request.getCateIds()) {
            BookCategory bookCategory = new BookCategory();
            bookCategory.setBookId(id);
            bookCategory.setCategoryId(categoryId);

            bookCategoriesEntity.add(bookCategory);
        }
        bookCategoryRepository.saveAll(bookCategoriesEntity);

        return BookCategoryDto.builder()
                .id(savedBook.getId())
                .cateIds(request.getCateIds())
                .title(savedBook.getTitle())
                .author(savedBook.getAuthor())
                .isbn(savedBook.getIsbn())
                .price(savedBook.getPrice())
                .yearOfPublish(savedBook.getYearOfPublish())
                .quantity(savedBook.getQuantity())
                .quantityAvail(savedBook.getQuantityAvail())
                .created(savedBook.getCreated())
                .modified(savedBook.getModified())
                .build();
    }

    @Override
    public BookResponse getBookById(Integer id) {
        return bookMapper.toBookResponse(
                bookRepository.findById(id).orElseThrow(() -> new BizException(ErrorCodeConst.INVALID_INPUT, "Book not existed")));
    }

    @Override
    public void deleteBook(Integer id) {

        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book not existed");
        }

        bookRepository.deleteById(id);
    }

    @Override
    public PageNewResponse<BookResponse> searchAllBooks(SearchBookRequest searchReq) {

        log.info("start search ightk bill. body: {}", JsonUtils.objToString(searchReq));
        PageRequest pageable = Common.getPageRequest(searchReq.getPage() - 1, searchReq.getLimit(), null);

        Pair<Long, List<BookResponse>> booksData = bookCustomRepositoryImpl.getBooksByConds(searchReq, pageable);
        Long countBooks = booksData.getFirst();
        List<BookResponse> listBooks = booksData.getSecond();

        Page<BookResponse> pageBookDto = new PageImpl<>(listBooks, pageable, countBooks);

        PageNewResponse<BookResponse> ib = PageNewResponse.<BookResponse>builder()
                .data(listBooks)
                .build();

        if (Objects.nonNull(searchReq.getGetTotalCount()) && Boolean.TRUE.equals(searchReq.getGetTotalCount())) {
            ib.setPagination(this.buildPagination(pageBookDto.getSize(), pageBookDto.getTotalPages(),
                    pageBookDto.getNumber() + 1, pageBookDto.getTotalElements()));
        }

        log.info("end ...");
        return ib;
    }

    @Override
    @Transactional
    public String deleteBooksByIds(IdsRequest ids) {
        // check all ids not exists
        // check list category ids exists db
        for (Integer bookId : ids.getIds()) {
            Optional<Book> book = bookRepository.findById(bookId);
            if (book.isEmpty()) {
                throw new BizException(ErrorCodeConst.VALIDATE_VIOLATION, "Book id " + bookId + " does not exist.");
            }
        }

        bookCategoryRepository.deleteAllByBookId(ids.getIds());
        bookRepository.deleteAllById(ids.getIds());

        StringBuilder message = new StringBuilder();
        message.append("Delete books ids success");

        return message.toString();

    }

    @Override
    @Transactional
    public String bulkBookService() {
        Random random = new Random();

        List<Book> booksEntity = new ArrayList<>();
        // get list category id
        List<Integer> cateIds = categoryRepository.findIdsCategory();

        if (cateIds.isEmpty()) {
            throw new BizException(ErrorCodeConst.VALIDATE_VIOLATION, "Category id null, dont create book");
        }

        Integer maxTitleNumber = bookRepository.findMaxTitleNumber();

        int maxTitleId = maxTitleNumber != null ? maxTitleNumber.intValue() : 0;
        System.out.println(maxTitleId + 123);
        for (int i = 0; i < 10000; i++) {
            Book book = new Book();

            book.setTitle("title-book-bulk-" + (maxTitleId + i));
            book.setAuthor("author-book-bulk-" + (maxTitleId + i));
            book.setPrice((long) random.nextInt(1000));
            book.setQuantity(random.nextInt(100));
            book.setIsbn(isbnGenerator.generateISBN());
            book.setQuantityAvail(20);
            book.setYearOfPublish(getRandomYear());
            book.setDescription("description-bulk-" + maxTitleId + i);

            booksEntity.add(book);
        }

        // handle batch data
        int i = 0;

        for (Book book : booksEntity) {
            entityManager.persist(book);
            i++;
            if (i % BATCH_SIZE == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
        entityManager.flush();
        entityManager.clear();
        //bookRepository.saveAll(booksEntity);

        return "Bulk insert data successfully!";
    }

    public int getRandomYear() {
        int currentYear = Year.now().getValue();
        int startYear = 2010;
        Random random = new Random();
        return startYear + random.nextInt(currentYear - startYear + 1);
    }

    private Map<String, Long> buildPagination(Integer limit, Integer totalPage, Integer currentPage, Long totalRecord){
        log.info("start buildPagination ...");

        Map<String, Long> pagination = new HashMap<>();
        pagination.put("limit", Long.valueOf(limit));
        pagination.put("total_page", Long.valueOf(totalPage));
        pagination.put("current_page", Long.valueOf(currentPage));
        pagination.put("total_record", totalRecord);

        log.info("end buildPagination ...");
        return pagination;
    }

}
