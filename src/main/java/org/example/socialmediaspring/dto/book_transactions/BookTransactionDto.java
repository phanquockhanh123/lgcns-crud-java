package org.example.socialmediaspring.dto.book_transactions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookTransactionDto {
    private Integer id;
    private String bookTitle;
    private String bookAuthor;
    private String bookIsbn;
    private Long bookPrice;
    private String email;
    private String phone;
    private UUID transactionId;
    private Integer status;
    private Integer quantity;
    private Integer amount;
    private Integer bonus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime returnDate;
}
