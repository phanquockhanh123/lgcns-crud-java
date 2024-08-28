package org.example.socialmediaspring.dto.product;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchProductDto {
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

    private Integer categoryId;

    private String categoryName;
}
