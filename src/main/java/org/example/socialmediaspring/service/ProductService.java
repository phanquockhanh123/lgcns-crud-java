package org.example.socialmediaspring.service;

import org.example.socialmediaspring.entity.Product;

import java.util.List;

public interface ProductService {
    Product saveProductWithKinds(Product product);

    List<Product> findAllProducts();
}
