package org.example.socialmediaspring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Type;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest{

        @NotNull(message = "Category name is required")
        private Integer categoryId;

        @NotBlank(message = "Book title is required")
        private String title;

        @NotBlank(message = "Book author is required")
        private String author;

        private String isbn;

        private String description;

        @NotNull(message = "Price is required")
        private Long price;
}