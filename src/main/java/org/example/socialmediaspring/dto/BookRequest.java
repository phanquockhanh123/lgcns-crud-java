package org.example.socialmediaspring.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest{

        private Integer categoryId;

        private String title;

        private String author;

        private String isbn;

        private String description;

        private Long price;
}