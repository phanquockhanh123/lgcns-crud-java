package org.example.socialmediaspring.dto.product;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.example.socialmediaspring.entity.Category;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CUProductRequest {
        @NotBlank(message = "Title is mandatory")
        private String title;

        private String description;

        @NotBlank(message = "Price is mandatory")
        private Long price;

        @NotBlank(message = "Discount Percentage is mandatory")
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

        private MultipartFile thumbnail;

        @NotBlank(message = "Category is mandatory")
        private Integer category;
}
