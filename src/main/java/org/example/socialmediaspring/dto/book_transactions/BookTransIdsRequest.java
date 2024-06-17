package org.example.socialmediaspring.dto.book_transactions;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookTransIdsRequest {

    private List<String> bookTransIds;
}
