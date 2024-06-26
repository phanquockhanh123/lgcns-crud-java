package org.example.socialmediaspring.service;

import org.example.socialmediaspring.common.PageNewResponse;
import org.example.socialmediaspring.common.PageResponse;
import org.example.socialmediaspring.dto.book_transactions.*;
import org.example.socialmediaspring.dto.user.BestCustomerRes;
import org.example.socialmediaspring.entity.BookTransaction;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface BookTransactionService {

    BookTransaction borrowBook(BookTransactionRequest request, Principal connectedUser);

    BookTransactionDetailDto returnBook(Integer id);

    PageNewResponse<BookTransactionDto> getBookTransByConds(SearchBookTransactionDto request, Principal connectedUser);

    void sendMaiNoticeOTBorrowBook();

    void sendMailNoticeSingle(Integer id);



}
