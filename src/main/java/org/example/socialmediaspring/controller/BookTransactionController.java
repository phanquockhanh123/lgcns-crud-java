package org.example.socialmediaspring.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.socialmediaspring.common.ResponseFactory;
import org.example.socialmediaspring.dto.book.BookRequest;
import org.example.socialmediaspring.dto.book_transactions.BookTransIdsRequest;
import org.example.socialmediaspring.dto.book_transactions.BookTransSearchRequest;
import org.example.socialmediaspring.dto.book_transactions.BookTransactionRequest;
import org.example.socialmediaspring.service.BookTransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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
            @Valid @RequestBody BookTransactionRequest request,
            Principal connectedUser
    ) {
        return responseFactory.success(bookTransactionService.borrowBook(request, connectedUser));
    }

    @GetMapping
    public ResponseEntity searchBookTransByConds(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "20", required = false) int size,
            @RequestParam(name = "status", required = false) Integer status,
            @RequestParam(name = "tranIds", required = false) List<String> tranIds,
            @RequestParam(name = "userIds", required = false) List<Integer> userIds
    ) {
        return responseFactory.success(bookTransactionService.getBookTransByConds(page, size, status, tranIds, userIds));
    }
}
