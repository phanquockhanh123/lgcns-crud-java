package org.example.socialmediaspring.dto.book_transactions;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookTransSearchRequest {
    private Integer status;
    private List<String> transIds;
    private Long endDate;
    private int size;
    private int page;
}
