package org.example.socialmediaspring.service;

import org.example.socialmediaspring.common.PageResponse;
import org.example.socialmediaspring.dto.BookRequest;
import org.example.socialmediaspring.dto.BookResponse;
import org.example.socialmediaspring.entity.BookEntity;

public interface BookService {
    BookEntity saveBook(BookRequest bookRequest);

    PageResponse<BookResponse> findAllBooks(int page, int size, String title, String author);
}
