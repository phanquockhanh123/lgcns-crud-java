package org.example.socialmediaspring.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryRequest {
    @NotBlank(message = "Category name is required")
    @NotNull(message = "Category name is required")
    private String name;

    private String description;
}
