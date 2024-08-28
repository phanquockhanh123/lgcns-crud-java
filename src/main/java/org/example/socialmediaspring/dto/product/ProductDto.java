package org.example.socialmediaspring.dto.product;

import lombok.*;
import org.example.socialmediaspring.entity.Category;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private Long id;

    private String title;

    private String description;

    private Long price;

    private Long discountPercentage;

    private Double rating;

    private Long stock;

    private String brand;

    private String sku;

    private Double weight;

    private String warrantyInformation;

    private String shippingInformation;

    private String availabilityStatus;

    private String returnPolicy;

    private Integer minimumOrderQuantity;


    private String categoryName;
}
