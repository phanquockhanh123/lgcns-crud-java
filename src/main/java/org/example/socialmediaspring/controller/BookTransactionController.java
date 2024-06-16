package org.example.socialmediaspring.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.socialmediaspring.common.ResponseFactory;
import org.example.socialmediaspring.dto.book.BookRequest;
import org.example.socialmediaspring.dto.book_transactions.BookTransactionRequest;
import org.example.socialmediaspring.service.BookTransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/admin/book_transactions")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
public class BookTransactionController {
    private final ResponseFactory responseFactory;

    private final BookTransactionService bookTransactionService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('user:create')")
    public ResponseEntity borrowBook(
            @Valid @RequestBody BookTransactionRequest request
    ) {
        return responseFactory.success(bookTransactionService.borrowBook(request));
    }
}
