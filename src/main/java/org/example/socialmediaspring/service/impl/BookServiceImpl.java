package org.example.socialmediaspring.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.socialmediaspring.common.IsbnGenerator;
import org.example.socialmediaspring.common.PageResponse;
import org.example.socialmediaspring.constant.ErrorCodeConst;
import org.example.socialmediaspring.dto.*;
import org.example.socialmediaspring.entity.Book;
import org.example.socialmediaspring.exception.BizException;
import org.example.socialmediaspring.mapper.BookMapper;
import org.example.socialmediaspring.repository.BookRepository;
import org.example.socialmediaspring.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookServiceImpl implements BookService {
    private final BookMapper bookMapper;

    private final BookRepository bookRepository;

    private  final EntityManager entityManager;

    private final IsbnGenerator isbnGenerator;

    @Override
    public Book saveBook(BookRequest bookRequest) {
        if (bookRepository.existsByTitle(bookRequest.getTitle())) {
            throw new BizException(ErrorCodeConst.INVALID_INPUT, "Book name existed");
        }

        Book book = bookMapper.toBook(bookRequest);

        book.setIsbn(isbnGenerator.generateISBN());

        return bookRepository.save(book);
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
    public BookResponse updateBook(Integer id, BookRequest request) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(request.getTitle());
                    book.setCategoryId(request.getCategoryId());
                    book.setPrice(request.getPrice());
                    book.setDescription(request.getDescription());
                    book.setAuthor(request.getAuthor());

                    Book newBook = bookRepository.save(book);
                    BookResponse rs = bookMapper.toBookResponse(newBook);
                    return rs;
                })
                .orElseThrow(() -> new EntityNotFoundException("Book not existed"));
    }

    @Override
    public BookResponse getBookById(Integer id) {
        return bookMapper.toBookResponse(
                bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not existed")));
    }

    @Override
    public void deleteBook(Integer id) {

        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book not existed");
        }

        bookRepository.deleteById(id);
    }

    @Override
    public PageResponse<BookCategoryDto> searchAllBooks(int page, int size, String title, String author, List<Integer> cateIds) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("created").descending());

        Page<BookCategoryDto> books = bookRepository.searchBooksByConds(pageable, title, author, cateIds);

        System.out.println("Result books: {}" + books);
        List<BookCategoryDto> booksResponse = books.stream().toList();
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
    public void deleteBooksByIds(BookIdsDto ids) {
        bookRepository.deleteAllById(ids.getIds());
    }

}
