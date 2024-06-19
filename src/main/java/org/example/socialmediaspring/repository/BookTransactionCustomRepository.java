package org.example.socialmediaspring.repository;

import org.example.socialmediaspring.dto.book_transactions.BookTransactionDto;
import org.example.socialmediaspring.dto.book_transactions.SearchBookTransactionDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;

import java.security.Principal;
import java.util.List;

public interface BookTransactionCustomRepository {
    Pair<Long, List<BookTransactionDto>> searchBookTransByConds(SearchBookTransactionDto searchReq, Pageable pageable, Principal connectedUser);
}
