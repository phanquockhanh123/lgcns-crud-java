package org.example.socialmediaspring.service;

import org.example.socialmediaspring.common.PageNewResponse;
import org.example.socialmediaspring.common.PageResponse;
import org.example.socialmediaspring.dto.book.BookCategoryDto;
import org.example.socialmediaspring.dto.book.SearchBookRequest;
import org.example.socialmediaspring.dto.common.IdsRequest;
import org.example.socialmediaspring.dto.book.BookRequest;
import org.example.socialmediaspring.dto.book.BookResponse;
import org.example.socialmediaspring.entity.Book;
import org.example.socialmediaspring.entity.BookCategory;
import org.example.socialmediaspring.entity.Category;

import java.util.List;

public interface BookService {
    BookCategoryDto saveBook(BookRequest bookRequest);

    BookCategoryDto updateBook(Integer id, BookRequest request);

    BookResponse getBookById(Integer id);

    void deleteBook(Integer id);

    PageNewResponse<BookResponse> searchAllBooks(SearchBookRequest request);

    String deleteBooksByIds(IdsRequest id);

    String bulkBookService();

    List<Category> getCategoriesByBookId(Integer id);


}
