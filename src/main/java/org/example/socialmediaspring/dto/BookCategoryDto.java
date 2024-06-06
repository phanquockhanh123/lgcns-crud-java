package org.example.socialmediaspring.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookCategoryDto {
    private Integer id;
    private String title;
    private String author;
    private String isbn;
    private Long price;
    private String categoryName;
    private LocalDateTime created;
}
