package org.example.socialmediaspring.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.example.socialmediaspring.common.BaseEntity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "book_transactions")
public class BookTransaction extends BaseEntity {
    private Integer bookId;
    private Integer userId;
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
