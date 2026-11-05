package org.example.restapi.testpractice.testsection03.valid;

import lombok.*;

import java.time.LocalDate;

/*
 * [문제 1] 각 필드에 검증 어노테이션을 붙이세요. (규칙은 README.md 참고)
 * */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductDTO {

    // 필수, "P-" + 숫자 4자리 (예: P-0001)
    private String productCode;

    // null·빈 문자열·공백 불가, 2~20자
    private String name;

    // 필수, 100 이상 1,000,000 이하
    // int 말고 Integer를 쓴 이유: int는 값이 없으면 0이 들어가서 "값 없음"을 검사할 수 없어요.
    private Integer price;

    // 오늘 또는 과거 날짜만 (JSON 형식: "2026-01-15")
    private LocalDate releaseDate;
}
