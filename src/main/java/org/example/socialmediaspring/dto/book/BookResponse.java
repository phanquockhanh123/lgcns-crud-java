package org.example.socialmediaspring.dto.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {
    private Integer id;
    private Integer categoryId;
    private String title;
    private String author;
    private String isbn;
    private String description;
    private Long price;
    private Integer yearOfPublish;
    private Integer quantity;
    private Integer quantityAvail;
}
