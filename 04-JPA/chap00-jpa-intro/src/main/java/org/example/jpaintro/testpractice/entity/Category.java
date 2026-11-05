package org.example.jpaintro.testpractice.entity;

import jakarta.persistence.*;
import lombok.Getter;

/*
 * [연습문제] tbl_category 테이블과 매핑되는 엔티티
 *
 * | DB 컬럼 (tbl_category) | 자바 필드        | 설명                                   |
 * |------------------------|------------------|----------------------------------------|
 * | category_code (PK, AI) | categoryCode     | 카테고리 번호 (DB 가 자동 생성)        |
 * | category_name          | categoryName     | 카테고리 이름                          |
 * | ref_category_code      | refCategoryCode  | 상위 카테고리 번호 (최상위면 NULL)     |
 *
 * 참고 코드 : org.example.jpaintro.entity.Menu
 */
@Entity
@Getter
// [문제 1-1] 이 엔티티가 "tbl_category" 테이블과 매핑되도록 어노테이션을 추가하세요.
// [문제 1-2] JPA 용 기본 생성자를 만들되, 외부에서 new Category() 를 못 하도록 PROTECTED 로 막으세요. (Lombok 사용)
public class Category {

    @Id
    // [문제 1-3] PK 를 DB 의 AUTO_INCREMENT 에 맡기도록 어노테이션을 추가하세요.
    // [문제 1-4] 아래 세 필드에 DB 컬럼명을 연결하는 어노테이션을 추가하세요.
    private Integer categoryCode;

    private String categoryName;

    private Integer refCategoryCode;

    // [문제 1-5] 등록용 생성자를 만드세요. (categoryCode 는 받지 않음, 값 세팅은 change() 재사용)

    // [문제 1-6] 이름과 상위 카테고리 번호를 바꾸는 change(String categoryName, Integer refCategoryCode) 메서드를 만드세요.
}
