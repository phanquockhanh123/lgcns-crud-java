package org.example.socialmediaspring.controller;

import lombok.RequiredArgsConstructor;
import org.example.socialmediaspring.common.ResponseFactory;
import org.example.socialmediaspring.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/public")
@RequiredArgsConstructor
public class BulkInsertDataController {
    private final BookService bookService;

    private final ResponseFactory responseFactory;

    @PostMapping("/books/bulk-insert")
    public ResponseEntity bulkBookService( @RequestPart("file") MultipartFile file) throws IOException {
        return responseFactory.success(bookService.bulkBookService(file));
    }
}
