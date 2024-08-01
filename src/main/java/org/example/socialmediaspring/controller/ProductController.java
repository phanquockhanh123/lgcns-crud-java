package org.example.socialmediaspring.controller;

import lombok.RequiredArgsConstructor;
import org.example.socialmediaspring.common.ResponseFactory;
import org.example.socialmediaspring.entity.Product;
import org.example.socialmediaspring.repository.KindRepository;
import org.example.socialmediaspring.repository.ProductRepository;
import org.example.socialmediaspring.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/products")
@RequiredArgsConstructor

public class ProductController {

    private final ProductService productService;

    private final ResponseFactory responseFactory;

    @PostMapping
    public ResponseEntity saveProductWithKinds(@RequestBody Product product){
        return responseFactory.success(productService.saveProductWithKinds(product));
    }

    @GetMapping
    public ResponseEntity findAllProducts(){
        return responseFactory.success(productService.findAllProducts());
    }


}
