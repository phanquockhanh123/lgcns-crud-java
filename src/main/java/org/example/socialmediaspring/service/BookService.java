package org.example.socialmediaspring.service;

import org.example.socialmediaspring.common.PageResponse;
import org.example.socialmediaspring.dto.book.BookCategoryDto;
import org.example.socialmediaspring.dto.common.IdsRequest;
import org.example.socialmediaspring.dto.book.BookRequest;
import org.example.socialmediaspring.dto.book.BookResponse;
import org.example.socialmediaspring.entity.Book;

import java.util.List;

public interface BookService {
    BookCategoryDto saveBook(BookRequest bookRequest);

    PageResponse<BookResponse> findAllBooks(int page, int size, String title, String author);

    BookCategoryDto updateBook(Integer id, BookRequest request);

    BookResponse getBookById(Integer id);

    void deleteBook(Integer id);

    //PageResponse<BookCategoryDto> searchAllBooks(int page, int size, String title, String author, List<Integer> cateIds, Integer yearFrom, Integer yearTo);

    String deleteBooksByIds(IdsRequest id);

    String bulkBookService();

}
