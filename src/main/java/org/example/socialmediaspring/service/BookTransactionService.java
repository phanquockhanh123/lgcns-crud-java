package org.example.socialmediaspring.service;

import org.example.socialmediaspring.dto.book_transactions.BookTransactionDto;
import org.example.socialmediaspring.dto.book_transactions.BookTransactionRequest;
import org.example.socialmediaspring.entity.BookTransaction;

public interface BookTransactionService {

    BookTransaction borrowBook(BookTransactionRequest request);

}
