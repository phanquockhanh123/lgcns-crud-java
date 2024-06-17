package org.example.socialmediaspring.dto.book_transactions;

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
    private String email;
    private String phone;
    private UUID transactionId;
    private Integer status;
    private Integer quantity;
    private Integer amount;
    private Integer bonus;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime returnDate;

}
