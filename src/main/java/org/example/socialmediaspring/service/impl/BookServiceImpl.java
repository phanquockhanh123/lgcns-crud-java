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
import org.example.socialmediaspring.dto.book.*;
import org.example.socialmediaspring.dto.common.IdsRequest;
import org.example.socialmediaspring.dto.emails.EmailDetails;
import org.example.socialmediaspring.entity.Book;
import org.example.socialmediaspring.entity.BookCategory;
import org.example.socialmediaspring.entity.Category;
import org.example.socialmediaspring.exception.BizException;
import org.example.socialmediaspring.mapper.BookMapper;
import org.example.socialmediaspring.repository.*;
import org.example.socialmediaspring.service.BookService;
import org.example.socialmediaspring.utils.FileUploadUtil;
import org.example.socialmediaspring.utils.JsonUtils;
import org.springframework.data.domain.*;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public BookCategoryDto saveBook(CUBookRequest bookRequest, MultipartFile file) throws IOException {


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

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Book book = new Book();
        book.setTitle(bookRequest.getTitle());
        book.setAuthor(bookRequest.getAuthor());
        book.setPrice(bookRequest.getPrice());
        book.setQuantity(bookRequest.getQuantity());
        book.setDescription(bookRequest.getDescription());
        book.setYearOfPublish(bookRequest.getYear());
        book.setIsbn(isbnGenerator.generateISBN());
        book.setQuantityAvail(book.getQuantity());
        book.setFilePath(fileName);
        Book savedBook =  bookRepository.save(book);

        String uploadDir = "src/main/resources/static/public/book-images/" + book.getId();

        FileUploadUtil.saveFile(uploadDir, fileName, file);

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
    public BookCategoryDto updateBook(Integer id, CUBookRequest request, MultipartFile file) throws IOException {
        Book existsBook = bookRepository.findById(id)
                .orElseThrow(() -> new BizException(ErrorCodeConst.INVALID_INPUT, "Book not found with id " + id));

        // check another title same exists
        bookRepository.findBookByTitle(request.getTitle()).ifPresent(book -> {
            if (!book.getId().equals(id)) {
                throw new BizException(ErrorCodeConst.INVALID_INPUT,"Book with title " + request.getTitle() + " already exists");
            }
        });

        // check empty object list cateids
        if (request.getCateIds() == null || request.getCateIds().isEmpty()) {
            throw new BizException(ErrorCodeConst.INVALID_INPUT,"No category name has been chosen");
        }
        // check list category ids exists db
        for (Integer categoryId : request.getCateIds()) {
            Optional<Category> category = categoryRepository.findById(categoryId);
            if (category.isEmpty()) {
                throw new BizException(ErrorCodeConst.VALIDATE_VIOLATION, "Category id " + categoryId + " does not exist.");
            }
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());


        existsBook.setTitle(request.getTitle());
        existsBook.setAuthor(request.getAuthor());
        existsBook.setPrice(request.getPrice());
        existsBook.setDescription(request.getDescription());
        existsBook.setQuantity(existsBook.getQuantity() + request.getQuantity());
        existsBook.setQuantityAvail(existsBook.getQuantityAvail() + request.getQuantity());
        existsBook.setYearOfPublish(request.getYear());
        existsBook.setFilePath(fileName);

        Book savedBook =  bookRepository.save(existsBook);

        String uploadDir = "src/main/resources/static/public/book-images/" + existsBook.getId();

        FileUploadUtil.saveFile(uploadDir, fileName, file);

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
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book not existed");
        }

        return bookRepository.findBooksInfoById(id);
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

        log.info("start search all books. body: {}", JsonUtils.objToString(searchReq));
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

        int maxTitleNumber = bookRepository.findMaxTitleNumber();

        for (int i = 0; i < 10000; i++) {
            Book book = new Book();

            book.setTitle("title-book-bulk-" + (maxTitleNumber + i));
            book.setAuthor("author-book-bulk-" + (maxTitleNumber + i));
            book.setPrice((long) random.nextInt(1000));
            book.setQuantity(random.nextInt(100));
            book.setIsbn(isbnGenerator.generateISBN());
            book.setQuantityAvail(20);
            book.setYearOfPublish(getRandomYear());
            book.setDescription("description-bulk-" + (maxTitleNumber + i));

            booksEntity.add(book);


        }

        // handle batch data
        int i = 0;

        List<BookCategory> bookCatesEntity = new ArrayList<>();
        for (Book book : booksEntity) {
            int randomCateId = random.nextInt(cateIds.size());
            entityManager.persist(book);
            i++;
            if (i % BATCH_SIZE == 0) {
                entityManager.flush();
                entityManager.clear();
            }
            // add book_categories with random cateIds
            BookCategory bookCategory = new BookCategory();
            bookCategory.setBookId(book.getId());
            bookCategory.setCategoryId(cateIds.get(randomCateId));
            bookCatesEntity.add(bookCategory);
        }

        for(BookCategory bookCategory : bookCatesEntity) {
            entityManager.persist(bookCategory);
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

    @Override
    public List<Category> getCategoriesByBookId(Integer id) {

        Book existsBook = bookRepository.findById(id)
                .orElseThrow(() -> new BizException(ErrorCodeConst.INVALID_INPUT, "Book not found with id " + id));

        List<Category> cates = bookRepository.getCatesByBookId(id);

        return cates;
    }

}
