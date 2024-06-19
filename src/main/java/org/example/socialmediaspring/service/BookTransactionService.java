package org.example.socialmediaspring.service;

import org.example.socialmediaspring.common.PageNewResponse;
import org.example.socialmediaspring.common.PageResponse;
import org.example.socialmediaspring.dto.book_transactions.*;
import org.example.socialmediaspring.entity.BookTransaction;

import java.security.Principal;
import java.util.List;

public interface BookTransactionService {

    BookTransaction borrowBook(BookTransactionRequest request, Principal connectedUser);

    BookTransaction returnBook(BookTransIdsRequest request);

    PageNewResponse<BookTransactionDto> getBookTransByConds(SearchBookTransactionDto request, Principal connectedUser);

}
