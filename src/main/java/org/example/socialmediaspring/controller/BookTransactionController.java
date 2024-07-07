package org.example.socialmediaspring.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.socialmediaspring.common.ResponseFactory;
import org.example.socialmediaspring.dto.book.BookRequest;
import org.example.socialmediaspring.dto.book.SearchBookRequest;
import org.example.socialmediaspring.dto.book_transactions.BookTransIdsRequest;
import org.example.socialmediaspring.dto.book_transactions.BookTransSearchRequest;
import org.example.socialmediaspring.dto.book_transactions.BookTransactionRequest;
import org.example.socialmediaspring.dto.book_transactions.SearchBookTransactionDto;
import org.example.socialmediaspring.dto.notifications.BookBorrowedNotification;
import org.example.socialmediaspring.service.BookTransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

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
            @RequestParam(name = "limit", defaultValue = "20", required = false) int limit,
            @RequestParam(name = "get_total_count", defaultValue = "false", required = false) Boolean getTotalCount,
            @RequestParam(name = "userId", required = false) Integer userId,
            @RequestParam(name = "status", required = false) Integer status,
            Principal connectedUser
    ) {
        return responseFactory.success(bookTransactionService.getBookTransByConds(new SearchBookTransactionDto(limit, page, getTotalCount, userId, status), connectedUser));
    }

    @PostMapping("/send-mail-notice/{id}")
    public ResponseEntity sendMailNotice(
            @Valid @PathVariable Integer id
    ) {
        bookTransactionService.sendMailNoticeSingle(id);
        return responseFactory.success("Send email notice book transaction expired time");
    }

    @PostMapping("/return-book/{id}")
    @PreAuthorize("hasAnyAuthority('user:update')")
    public ResponseEntity returnBook(
            @Valid @PathVariable Integer id
    ) {
        return responseFactory.success(bookTransactionService.returnBook(id));
    }


}
