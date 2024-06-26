package org.example.socialmediaspring.dto.book;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookBestSerllerRes {
    private Integer id;
    private String title;
    private String author;
    private String isbn;
    private String filePath;
    private Long price;
    private Long totalSale;
    private Long totalMoney;
}
