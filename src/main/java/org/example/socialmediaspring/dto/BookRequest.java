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
        @NotEmpty(message = "title not empty")
        private String title;
        @NotEmpty(message = "author name not empty")
        private String authorName;
        @NotEmpty(message = "isbn not empty")
        private String isbn;
        @NotEmpty(message = "synopsis not empty")
        private String synopsis;
        @NotEmpty(message = "book cover not empty")
        private String bookCover;
}