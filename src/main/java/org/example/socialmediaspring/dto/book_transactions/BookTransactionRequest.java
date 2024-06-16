package org.example.socialmediaspring.dto.book_transactions;


import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookTransactionRequest {
    private Integer bookId;
    private Integer userId;
    private Integer quantity;
    private String startDate;
    private String endDate;
    private Integer price;

}
