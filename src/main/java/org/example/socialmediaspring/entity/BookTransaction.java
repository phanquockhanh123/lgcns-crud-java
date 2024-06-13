package org.example.socialmediaspring.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.socialmediaspring.common.BaseEntity;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book_transactions")
public class BookTransaction extends BaseEntity {
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
