package org.example.socialmediaspring.repository;


import org.example.socialmediaspring.dto.book.BookResponse;
import org.example.socialmediaspring.dto.book.SearchBookRequest;
import org.modelmapper.internal.Pair;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookCustomRepository {
    Pair<Long, List<BookResponse>> getBooksByConds(SearchBookRequest searchReq, Pageable pageable);
}
