package org.example.socialmediaspring.repository;


import org.example.socialmediaspring.dto.book.BookBestSellerRes;
import org.example.socialmediaspring.dto.book.BookResponse;
import org.example.socialmediaspring.dto.book.FilterBookReportResquest;
import org.example.socialmediaspring.dto.book.SearchBookRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;

import java.util.List;

public interface BookCustomRepository {
    Pair<Long, List<BookResponse>> getBooksByConds(SearchBookRequest searchReq, Pageable pageable);

    Pair<Long, List<BookBestSellerRes>> getBooksReport(FilterBookReportResquest searchReq, Pageable pageable);
}
