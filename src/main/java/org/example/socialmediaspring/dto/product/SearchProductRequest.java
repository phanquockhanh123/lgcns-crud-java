package org.example.socialmediaspring.dto.product;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchProductRequest {
    private Integer limit = 20;
    private Integer page = 1;
    private Boolean getTotalCount;
    private String title;
    private Integer category;
    private Integer priceFrom;
    private Integer priceTo;
}
