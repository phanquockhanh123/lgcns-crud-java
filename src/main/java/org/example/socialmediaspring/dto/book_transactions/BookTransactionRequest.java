package org.example.socialmediaspring.dto.book_transactions;


import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookTransactionRequest {
    @NotNull(message = "Book id is required")
    private Integer bookId;
    @NotNull(message = "Quantity is required")
    private Integer quantity;
    private Long startDate;
    private Long endDate;



}
