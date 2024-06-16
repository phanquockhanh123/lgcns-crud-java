package org.example.socialmediaspring.dto.book_transactions;

import lombok.*;

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
    private Integer bookId;
    private Integer userId;
    private UUID transactionId;
    private Integer type;
    private Integer status;
    private Integer quantity;
    private Integer amount;
    private Integer bonus;
    private Date startDate;
    private Date endDate;
    private Date returnDate;
}
