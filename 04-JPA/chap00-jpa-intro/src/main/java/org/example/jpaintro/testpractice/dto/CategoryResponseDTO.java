package org.example.jpaintro.testpractice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.jpaintro.testpractice.entity.Category;

/*
 * [연습문제] 카테고리 응답 DTO
 *
 * 예) { "categoryCode": 13, "categoryName": "디저트", "refCategoryCode": 2 }
 * 참고 코드 : org.example.jpaintro.dto.MenuResponseDTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO {

    private Integer categoryCode;
    private String categoryName;
    private Integer refCategoryCode;

    // [문제 3] Entity(Category) → CategoryResponseDTO 로 변환하는 정적 팩토리 메서드를 완성하세요.
    public static CategoryResponseDTO from(Category category) {
        throw new UnsupportedOperationException("[문제 3] 을 풀어주세요.");
    }
}
