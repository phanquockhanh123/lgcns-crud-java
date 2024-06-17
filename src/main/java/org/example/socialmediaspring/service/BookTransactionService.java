package org.example.socialmediaspring.service;

import org.example.socialmediaspring.common.PageResponse;
import org.example.socialmediaspring.dto.book_transactions.BookTransIdsRequest;
import org.example.socialmediaspring.dto.book_transactions.BookTransSearchRequest;
import org.example.socialmediaspring.dto.book_transactions.BookTransactionDto;
import org.example.socialmediaspring.dto.book_transactions.BookTransactionRequest;
import org.example.socialmediaspring.entity.BookTransaction;

import java.security.Principal;
import java.util.List;

public interface BookTransactionService {

    BookTransaction borrowBook(BookTransactionRequest request, Principal connectedUser);

    BookTransaction returnBook(BookTransIdsRequest request);

    PageResponse<BookTransaction> getBookTransByConds(int page,int  size,Integer status,List<String> tranIds, List<Integer> userIds);

}
