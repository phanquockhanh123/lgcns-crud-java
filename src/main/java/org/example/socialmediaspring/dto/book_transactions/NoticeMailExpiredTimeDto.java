package org.example.socialmediaspring.dto.book_transactions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NoticeMailExpiredTimeDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
    private Integer quantity;
    private Integer amount;
    private String bookIsbn;
    private String bookTitle;
    private String bookAuthor;
    private Long bookPrice;

}
