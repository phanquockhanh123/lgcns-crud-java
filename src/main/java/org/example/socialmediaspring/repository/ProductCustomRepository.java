package org.example.socialmediaspring.repository;

import org.example.socialmediaspring.dto.book.BookResponse;
import org.example.socialmediaspring.dto.book.SearchBookRequest;
import org.example.socialmediaspring.dto.product.ProductDto;
import org.example.socialmediaspring.dto.product.SearchProductDto;
import org.example.socialmediaspring.dto.product.SearchProductRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;

import java.util.List;

public interface ProductCustomRepository {
    Pair<Long, List<SearchProductDto>> getProductsByConds(SearchProductRequest searchReq, Pageable pageable);
}
