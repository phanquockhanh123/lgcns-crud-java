package org.example.socialmediaspring.dto.book;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private List<Integer> cateIds;
    private String isbn;
    private Long price;
    private Integer quantity;
    private Integer quantityAvail;
    private Integer yearOfPublish;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modified;
}
