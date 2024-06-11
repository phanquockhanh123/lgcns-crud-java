package org.example.socialmediaspring.mapper;

import org.example.socialmediaspring.dto.book.BookRequest;
import org.example.socialmediaspring.dto.book.BookResponse;
import org.example.socialmediaspring.entity.Book;
import org.springframework.stereotype.Service;

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
                .yearOfPublish(request.getYear())
                .quantity(request.getQuantity())
                .quantityAvail(request.getQuantityAvail())
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
                .yearOfPublish(book.getYearOfPublish())
                .quantity(book.getQuantity())
                .quantityAvail(book.getQuantityAvail())
                .build();
    }

}
