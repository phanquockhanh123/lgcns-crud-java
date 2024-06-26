package org.example.socialmediaspring.repository;


import org.example.socialmediaspring.dto.book.BookResponse;
import org.example.socialmediaspring.dto.book.SearchBookRequest;
import org.example.socialmediaspring.dto.book_transactions.BookTransactionDto;
import org.example.socialmediaspring.dto.book_transactions.SearchBookTransactionDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;

import java.util.List;

public interface BookCustomRepository {
    Pair<Long, List<BookResponse>> getBooksByConds(SearchBookRequest searchReq, Pageable pageable);
}
