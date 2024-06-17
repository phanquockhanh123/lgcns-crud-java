package org.example.socialmediaspring.dto.book;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class BookCategoryDto {
    private Integer id;
    private String title;
    private String author;
    private String isbn;
    private Long price;
    private Integer quantity;
    private Integer quantityAvail;
    private Integer yearOfPublish;
    private Date created;
    private Date modified;
}
