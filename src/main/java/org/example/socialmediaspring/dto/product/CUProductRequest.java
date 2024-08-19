package org.example.socialmediaspring.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.example.socialmediaspring.entity.Category;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
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

        @NotBlank(message = "Category is mandatory")
        private Integer category;
}
