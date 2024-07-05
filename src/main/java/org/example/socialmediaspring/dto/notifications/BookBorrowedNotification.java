package org.example.socialmediaspring.dto.notifications;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookBorrowedNotification {
    private String bookName;

    private LocalDateTime timestamp;
}
