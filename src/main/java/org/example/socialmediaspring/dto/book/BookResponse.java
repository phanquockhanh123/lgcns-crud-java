package org.example.socialmediaspring.dto.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {
    private Integer id;
//    private List<String> cateNames;
    private String title;
    private String author;
    private String isbn;
    private String description;
    private Long price;
    private Integer yearOfPublish;
    private Integer quantity;
    private Integer quantityAvail;
    private Date created;
    private Date modified;
}
