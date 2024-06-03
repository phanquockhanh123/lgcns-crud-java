package org.example.socialmediaspring.mapper;

import org.example.socialmediaspring.dto.BookRequest;
import org.example.socialmediaspring.dto.BookResponse;
import org.example.socialmediaspring.entity.BookEntity;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {
    public BookEntity toBook(BookRequest request) {
        return BookEntity.builder()
                .title(request.getTitle())
                .isbn(request.getIsbn())
                .authorName(request.getAuthorName())
                .synopsis(request.getSynopsis())
                .bookCover(request.getBookCover())
                .build();
    }

    public BookResponse toBookResponse(BookEntity book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .isbn(book.getIsbn())
                .synopsis(book.getSynopsis())
                .bookCover(book.getBookCover())
                .build();
    }
}
