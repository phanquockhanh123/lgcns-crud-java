package org.example.socialmediaspring.service;

import org.example.socialmediaspring.common.PageNewResponse;
import org.example.socialmediaspring.dto.common.LongIdsRequest;
import org.example.socialmediaspring.dto.product.*;
import org.example.socialmediaspring.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    CreateProductDto saveProduct(CUProductRequest productDto, MultipartFile thumbnail);

    CreateProductDto updateProduct(Long id,CUProductRequest productDto, MultipartFile thumbnail);

    SearchProductDto getProductById(Long id);

    PageNewResponse<SearchProductDto> findAllProducts(SearchProductRequest request);

    String deleteProductsByIds(LongIdsRequest request);
}
