package org.example.socialmediaspring.dto.book;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.opencsv.bean.CsvBindByName;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookCsvRepresentation {
    @CsvBindByName(column = "id")
    private Integer id;
    @CsvBindByName(column = "title")
    private String title;
    @CsvBindByName(column = "author")
    private String author;
    @CsvBindByName(column = "isbn")
    private String isbn;
    @CsvBindByName(column = "description")
    private String description;
    @CsvBindByName(column = "price")
    private Long price;
    @CsvBindByName(column = "yearOfPublish")
    private Integer yearOfPublish;
    @CsvBindByName(column = "quantity")
    private Integer quantity;
    @CsvBindByName(column = "quantityAvail")
    private Integer quantityAvail;
    @CsvBindByName(column = "filePath")
    private String filePath;
}
