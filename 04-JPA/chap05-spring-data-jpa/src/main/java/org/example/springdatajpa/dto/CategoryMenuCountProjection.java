package org.example.springdatajpa.dto;

/*
 * 인터페이스 Projection
 * - 구현 클래스를 작성하지 않음 -> Spring Data JPA가 실행 시점에 인터페이스 구현 객체를 자동으로 생성
 * - JPQL select 항목의 별칭과 getter의 프로퍼티 이름을 맞춰야함.
 * */
public interface CategoryMenuCountProjection {

    // 카테고리 코드
    Integer getCategoryCode();

    // 카테고리 이름
    String getCategoryName();

    // 메뉴 개수
    Long getMenuCount();
}
