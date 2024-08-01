package org.example.socialmediaspring.repository;

import org.example.socialmediaspring.dto.product.ProductKindDto;
import org.example.socialmediaspring.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByTitleContaining(String title);

}
