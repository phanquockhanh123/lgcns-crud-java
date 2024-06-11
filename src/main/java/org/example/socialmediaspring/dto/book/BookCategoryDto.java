package org.example.socialmediaspring.dto.book;

import lombok.*;

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
    private Integer quantity;
    private Integer quantityAvail;
    private Integer yearOfPublish;
    private Date created;
    private Date modified;
}
