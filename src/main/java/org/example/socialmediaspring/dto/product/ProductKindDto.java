package org.example.socialmediaspring.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.socialmediaspring.entity.Kind;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductKindDto {
    private String title;

    private String description;

    private Long price;

    private Long discountPercentage;

    private Long rating;

    private Long stock;

    private String brand;

    private String sku;

    private Long weight;

    private String warrantyInformation;

    private String shippingInformation;

    private String availabilityStatus;

    private String returnPolicy;

    private Integer minimumOrderQuantity;

    private String thumbnail;

    private Set<Kind> kinds;
}
