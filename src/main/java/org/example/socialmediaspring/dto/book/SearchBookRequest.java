package org.example.socialmediaspring.dto.book;

import lombok.*;

import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchBookRequest {
    private Integer limit = 20;
    private Integer page = 1;
    private Boolean getTotalCount;
    private String title;
    private String author;
    private Integer yearFrom;
    private Integer yearTo;
}
