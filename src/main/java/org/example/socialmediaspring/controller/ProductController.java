package org.example.socialmediaspring.controller;

import lombok.RequiredArgsConstructor;
import org.example.socialmediaspring.common.ResponseFactory;
import org.example.socialmediaspring.dto.book.CUBookRequest;
import org.example.socialmediaspring.dto.common.IdsRequest;
import org.example.socialmediaspring.dto.common.LongIdsRequest;
import org.example.socialmediaspring.dto.product.CUProductRequest;
import org.example.socialmediaspring.dto.product.CreateProductDto;
import org.example.socialmediaspring.dto.product.SearchProductRequest;
import org.example.socialmediaspring.entity.Product;
import org.example.socialmediaspring.service.ProductService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/admin/products")
@CrossOrigin("*")
@RequiredArgsConstructor

public class ProductController {

    private final ProductService productService;

    private final ResponseFactory responseFactory;

    @PostMapping(path = "/create", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @PreAuthorize("hasAnyAuthority('admin:update', 'manager:update')")
    public ResponseEntity saveProduct(@ModelAttribute CUProductRequest productDto){
        return responseFactory.success(productService.saveProduct(productDto));
    }

    @GetMapping
    public ResponseEntity searchProductsByConds(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "limit", defaultValue = "20", required = false) int limit,
            @RequestParam(name = "get_total_count", required = false) Boolean getTotalCount,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "category", required = false) Integer category,
            @RequestParam(name = "priceFrom", required = false) Integer priceFrom,
            @RequestParam(name = "priceTo", required = false) Integer priceTo
    ){
        return responseFactory.success(productService.findAllProducts(new SearchProductRequest( limit, page, getTotalCount, title, category, priceFrom, priceTo)));
    }

    @PutMapping(path = "/update/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @PreAuthorize("hasAnyAuthority('admin:update', 'manager:update')")
    public ResponseEntity updateProduct(
            @PathVariable Long id,
            @ModelAttribute CUProductRequest request ) {
        return responseFactory.success(productService.updateProduct(id, request));
    }

    @GetMapping("/detail/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'manager:read', 'user:read')")
    public ResponseEntity getProduct(@PathVariable Long id) {
        return responseFactory.success(productService.getProductById(id));
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('admin:delete', 'manager:delete')")
    public ResponseEntity deleteProductsByIds(@RequestBody LongIdsRequest ids) {
        return responseFactory.success(productService.deleteProductsByIds(ids));
    }
}
