package org.example.socialmediaspring.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest{

        @NotNull(message = "List categories name is required")
        private List<Integer> cateIds;

        @NotBlank(message = "Book title is required")
        private String title;

        @NotBlank(message = "Book author is required")
        private String author;

        private String isbn;

        private String description;

        @NotNull(message = "Price is required")
        private Long price;

        @NotNull(message = "Year of publish is required")
        private Integer year;

        @NotNull(message = "Quantity is required")
        private Integer quantity;

        private Integer quantityAvail;

       //private MultipartFile filePath;


}