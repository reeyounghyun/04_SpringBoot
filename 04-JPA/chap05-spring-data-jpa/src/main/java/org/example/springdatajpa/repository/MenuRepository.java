package org.example.springdatajpa.repository;

import org.example.springdatajpa.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Integer> {

    @Query("select menu from Menu menu join fetch menu.category")
    List<Menu> findAllWithCategory();

    /*
    *   * Spring Data JPA의 쿼리메서드
     * - 정해진 규칙에 따라 메서드 이름을 작성하면 Spring Data JPA가 JPQL을 자동 생성함
     *
     * [이 메서드에서 각 요소의 위치]
     *  findByOrderableStatusOrderByMenuPriceAscMenuCodeAsc 를 잘라 보면
     *
     *  1.findBy __(시작 신호 ~로 찾아줘)
     *      - findBy     : "~로 찾아줘"라는 시작 신호. 뒤에 오는 단어가 검색 조건이 된다. (SQL의 SELECT ... WHERE)
     *
     *  2.[조건필드]OrderableStatus __(주문 가능 상태로 찾는다)
     *      - 조건필드   : 무엇을 기준으로 찾을지. DB 컬럼명이 아니라 엔티티의 필드명을 쓴다. (orderableStatus → OrderableStatus)
     *
     *  3.OrderBy __(여기부터 정렬)
     *      - OrderBy    : 여기서부터는 정렬 조건이라는 구분선. 정렬 기준이 여러 개여도 한 번만 쓴다.
     *
     *  4.[정렬필드 1] MenuPrice __(가격으로 정렬)
     *      - Asc / Desc : 정렬 방향. Asc는 작은 값부터(오름차순), Desc는 큰 값부터(내림차순).
     *
     *  5.[정렬 방향]Asc __(오름차순)
     *      - Asc / Desc : 정렬 방향. Asc는 작은 값부터(오름차순), Desc는 큰 값부터(내림차순).
     *
     *  6.[정렬필드 2] MenuCode __(가격이 같으면 메뉴코드로 정렬)
     *
     *  → "주문 가능 상태가 같은 메뉴를 찾아서, 가격이 싼 순으로, 가격이 같으면 메뉴코드 순으로 정렬해줘"
     *  → 파라미터(orderableStatus)는 조건필드(OrderableStatus) 자리에 들어간다.
     */

    /*
    * 키워드
    * And Or: 조건 연결
    * Containing: 문자열 포함검색
    * GreaterThanEqual / LessThanEqual: 이상 / 이하
    * Between: 범위 검색
    * */
    //  findBy + 조건필드 + 비교 키워드 + OrderBy + 정렬필드 + Asc/Desc
    List<Menu> findByOrderableStatusOrderByMenuPriceAscMenuCodeAsc(String orderableStatus);
}
