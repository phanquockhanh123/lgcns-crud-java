package org.example.socialmediaspring.mapper;

import org.example.socialmediaspring.dto.BookRequest;
import org.example.socialmediaspring.dto.BookResponse;
import org.example.socialmediaspring.entity.Book;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BookMapper {
    public Book toBook(BookRequest request) {
        return Book.builder()
                .categoryId(request.getCategoryId())
                .title(request.getTitle())
                .isbn(request.getIsbn())
                .author(request.getAuthor())
                .description(request.getDescription())
                .price(request.getPrice())
                .build();
    }

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .categoryId(book.getCategoryId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .description(book.getDescription())
                .price(book.getPrice())
                .build();
    }
}
