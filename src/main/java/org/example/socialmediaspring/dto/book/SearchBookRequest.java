package org.example.socialmediaspring.dto.book;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchBookRequest {
    private String title;
    private String author;
}
