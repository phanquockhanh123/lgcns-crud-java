package org.example.socialmediaspring.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.socialmediaspring.common.PageResponse;
import org.example.socialmediaspring.dto.BookRequest;
import org.example.socialmediaspring.dto.BookResponse;
import org.example.socialmediaspring.entity.BookEntity;
import org.example.socialmediaspring.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/admin/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookEntity> saveBook(
            @Valid @RequestBody BookRequest request
    ) {
        return ResponseEntity.ok(bookService.saveBook(request));
    }

    @GetMapping
    public ResponseEntity<PageResponse<BookResponse>> findAllBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "20", required = false) int size,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "author", required = false) String author
    ) {
        return ResponseEntity.ok(bookService.findAllBooks(page, size, title, author));
    }
}
