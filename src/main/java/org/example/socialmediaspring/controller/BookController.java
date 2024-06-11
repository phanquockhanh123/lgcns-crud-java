package org.example.socialmediaspring.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.socialmediaspring.common.PageResponse;
import org.example.socialmediaspring.common.ResponseFactory;
import org.example.socialmediaspring.dto.*;
import org.example.socialmediaspring.entity.Book;
import org.example.socialmediaspring.entity.Category;
import org.example.socialmediaspring.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/admin/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    private final ResponseFactory responseFactory;

    @PostMapping
    public ResponseEntity saveBook(
            @Valid @RequestBody BookRequest request
    ) {
        return responseFactory.success(bookService.saveBook(request));
    }

    @GetMapping
    public ResponseEntity findAllBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "20", required = false) int size,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "author", required = false) String author
    ) {
        return responseFactory.success(bookService.findAllBooks(page, size, title, author));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateBook(
            @PathVariable Integer id,
            @Valid @RequestBody  BookRequest request) {
        return responseFactory.success(bookService.updateBook(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity getBook(@PathVariable Integer id) {
        return responseFactory.success(bookService.getBookById(id));
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity deleteBook(@PathVariable Integer id) {
        bookService.deleteBook(id);
        return responseFactory.success(null);
    }

    @GetMapping("/search")
    public ResponseEntity searchBooksByConds(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "20", required = false) int size,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "author", required = false) String author,
            @RequestParam(name = "cateIds", required = false) List<Integer> cateIds
    ) {
        return responseFactory.success(bookService.searchAllBooks(page, size, title, author, cateIds));
    }

    @DeleteMapping
    public ResponseEntity deleteBooksByIds(@RequestBody BookIdsDto ids) {

        return responseFactory.success(bookService.deleteBooksByIds(ids));
    }
}
