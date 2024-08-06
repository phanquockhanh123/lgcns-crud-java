package org.example.socialmediaspring.service;

import org.example.socialmediaspring.common.PageNewResponse;
import org.example.socialmediaspring.dto.common.LongIdsRequest;
import org.example.socialmediaspring.dto.product.*;
import org.example.socialmediaspring.entity.Product;

import java.util.List;

public interface ProductService {
    CreateProductDto saveProduct(CUProductRequest productDto);

    CreateProductDto updateProduct(Long id,CUProductRequest productDto);

    ProductDto getProductById(Long id);

    PageNewResponse<SearchProductDto> findAllProducts(SearchProductRequest request);

    String deleteProductsByIds(LongIdsRequest request);
}
