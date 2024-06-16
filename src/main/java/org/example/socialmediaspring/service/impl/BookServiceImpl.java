package org.example.socialmediaspring.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.socialmediaspring.common.IsbnGenerator;
import org.example.socialmediaspring.common.PageResponse;
import org.example.socialmediaspring.constant.ErrorCodeConst;
import org.example.socialmediaspring.dto.book.BookCategoryDto;
import org.example.socialmediaspring.dto.common.IdsRequest;
import org.example.socialmediaspring.dto.book.BookRequest;
import org.example.socialmediaspring.dto.book.BookResponse;
import org.example.socialmediaspring.entity.Book;
import org.example.socialmediaspring.entity.BookCategory;
import org.example.socialmediaspring.entity.Category;
import org.example.socialmediaspring.exception.BizException;
import org.example.socialmediaspring.mapper.BookMapper;
import org.example.socialmediaspring.repository.BookCategoryRepository;
import org.example.socialmediaspring.repository.BookRepository;
import org.example.socialmediaspring.repository.CategoryRepository;
import org.example.socialmediaspring.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public PageResponse<BookResponse> findAllBooks(int page, int size, String title, String author) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("created").descending());


        Page<Book> books = bookRepository.findBooksByConds(pageable, title, author);
        List<BookResponse> booksResponse = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                booksResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
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
        bookCategoryRepository.deleteAllById(Collections.singleton(id));

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

//    @Override
//    public PageResponse<BookCategoryDto> searchAllBooks(int page, int size, String title, String author, List<Integer> cateIds, Integer yearFrom, Integer yearTo) {
//
//        Pageable pageable = PageRequest.of(page, size, Sort.by("created").descending());
//
//        Page<BookCategoryDto> books = bookRepository.searchBooksByConds(pageable, title, author, cateIds, yearFrom, yearTo);
//
//        System.out.println("Result books: {}" + books);
//        List<BookCategoryDto> booksResponse = books.stream().toList();
//        return new PageResponse<>(
//                booksResponse,
//                books.getNumber(),
//                books.getSize(),
//                books.getTotalElements(),
//                books.getTotalPages(),
//                books.isFirst(),
//                books.isLast()
//        );
//    }

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
}
