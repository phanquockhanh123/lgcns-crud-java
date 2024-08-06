package org.example.socialmediaspring.mapper;

import org.example.socialmediaspring.dto.product.CUProductRequest;
import org.example.socialmediaspring.dto.product.CreateProductDto;
import org.example.socialmediaspring.entity.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {
    public CreateProductDto toProductDto(Product newProduct) {
        return CreateProductDto.builder()
                .id(newProduct.getId())
                .title((newProduct.getTitle()))
                .description(newProduct.getDescription())
                .price(newProduct.getPrice())
                .discountPercentage(newProduct.getDiscountPercentage())
                .rating(newProduct.getRating())
                .stock(newProduct.getStock())
                .brand(newProduct.getBrand())
                .sku(newProduct.getSku())
                .weight(newProduct.getWeight())
                .warrantyInformation(newProduct.getWarrantyInformation())
                .shippingInformation(newProduct.getShippingInformation())
                .availabilityStatus(newProduct.getAvailabilityStatus())
                .returnPolicy(newProduct.getReturnPolicy())
                .minimumOrderQuantity(newProduct.getMinimumOrderQuantity())
                .build();
    }

}
