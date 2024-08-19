package org.example.socialmediaspring.repository;

import org.example.socialmediaspring.dto.product.ProductDto;
import org.example.socialmediaspring.dto.product.SearchProductDto;
import org.example.socialmediaspring.entity.Book;
import org.example.socialmediaspring.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByTitleContaining(String title);

    Boolean existsByTitle(String title);

    Optional<Product> findProductByTitle(String title);

    @Query(value = "SELECT new org.example.socialmediaspring.dto.product.SearchProductDto(p.id, p.title, p.description, p.price, p.discountPercentage," +
            " p.rating, p.stock, p.brand, p.sku, p.weight, p.warrantyInformation," +
            " p.shippingInformation, p.availabilityStatus, p.returnPolicy, p.minimumOrderQuantity," +
            " p.thumbnail, c.id, c.name)  FROM Product p " +
            " INNER JOIN Category c " +
            " ON p.categoryId = c.id " +
            " WHERE p.id = :id ")
    SearchProductDto getProductDetail(Long id);

}
