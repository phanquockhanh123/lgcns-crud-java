package org.example.socialmediaspring.dto.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookBestSellerRes {
    private Integer id;
    private String title;
    private String author;
    private String isbn;
    private Integer quantity;
    private Integer quantityAvail;
    private Integer yearOfPublish;
    private Long price;
    private Long totalSale;
    private Long totalMoney;
}
