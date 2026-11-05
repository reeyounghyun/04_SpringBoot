package org.example.jpaintro.testpractice.dto;

import lombok.*;

/*
 * [연습문제] 카테고리 등록/수정 요청 DTO (완성본 - 수정할 필요 없음)
 *
 * 예) { "categoryName": "디저트", "refCategoryCode": 2 }
 * - categoryCode 는 DB 가 자동 생성하므로 받지 않는다.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDTO {

    private String categoryName;
    private Integer refCategoryCode;
}
