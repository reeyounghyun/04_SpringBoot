package org.example.springdatajpa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//카테고리 정보와 계산 결과를 DTO로 반환한다
public class findCategoryPriceStatisticsDTO {
    private Integer categoryCode;
    private String categoryName;
    private Integer minPrice;
    private Integer maxPrice;
}
