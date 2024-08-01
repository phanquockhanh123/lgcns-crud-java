package org.example.socialmediaspring.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.socialmediaspring.dto.product.ProductKindDto;
import org.example.socialmediaspring.entity.Product;
import org.example.socialmediaspring.repository.KindRepository;
import org.example.socialmediaspring.repository.ProductRepository;
import org.example.socialmediaspring.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private KindRepository kindRepository;

    @Override
    public Product saveProductWithKinds(Product product) {
        return productRepository.save(product);

    }
    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }
}
