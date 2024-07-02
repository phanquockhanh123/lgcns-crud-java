package org.example.socialmediaspring.dto.book_transactions;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchBookTransactionDto {
    private Integer limit = 20;
    private Integer page = 1;
    private Boolean getTotalCount;
    private Integer userId;
    private Integer status;
}
