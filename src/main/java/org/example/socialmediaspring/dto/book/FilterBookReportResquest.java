package org.example.socialmediaspring.dto.book;

import lombok.*;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterBookReportResquest {
    private Integer limit = 20;
    private Integer page = 1;
    private Boolean getTotalCount;
    private String title;
    private String author;
    private Integer yearFrom;
    private Integer yearTo;
    private String sortOrder;
    private String sortField;
}
