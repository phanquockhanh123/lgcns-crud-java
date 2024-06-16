package org.example.socialmediaspring.controller;

import lombok.RequiredArgsConstructor;
import org.example.socialmediaspring.common.ResponseFactory;
import org.example.socialmediaspring.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/public")
@RequiredArgsConstructor
public class BulkInsertDataController {
    private final BookService bookService;

    private final ResponseFactory responseFactory;

    @GetMapping("/books/bulk-insert")
    public ResponseEntity bulkBookService() {
        return responseFactory.success(bookService.bulkBookService());
    }
}
