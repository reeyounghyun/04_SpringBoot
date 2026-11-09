package org.example.springdatajpa.repository;

import org.example.springdatajpa.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Integer> {


    // insert update delete 단순 기존데이터만 가져올수있고 집계를 할수도 있다
    // -> 안에서 응답받는 데이터가(엔티티 필드의 전체, 혹은 일부)
    // -> 엔티티 필드가 아닌 데이터 > 엔티티에 필드로 넣어야하나? 고민이 생김
    //    (쉽게 말해) 집계 결과(COUNT, AVG 등)처럼 엔티티에 없는 값을 조회하면 어디에 담아야 할지 고민이 생김
    //    엔티티에 필드를 추가하면 테이블 매핑이 깨지므로 X
    // -> 해결방법 프로젝션을 사용할거임.(이전에 했던 방식으로 인터페이스로 결과 클래스를 만드는것과 동일함)
    @Query("select menu from Menu menu join fetch menu.category")
    List<Menu> findAllWithCategory();

    /*
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
     *      - 정렬필드   : 무엇을 기준으로 줄 세울지. 앞에 쓴 필드가 먼저 적용되고, 값이 같을 때 다음 필드로 정렬한다.
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
     *  Spring Data JPA의 쿼리메서드
     * - 정해진 규칙에 따라 메서드 이름을 작성하면 Spring Data JPA가 JPQL을 자동 생성함
     * - findBy + 조건필드 + 비교 키워드 + OrderBy + 정렬필드 + Asc/Desc
     *
     * 키워드
     * And Or: 조건 연결
     * Containing: 문자열 포함검색
     * GreaterThanEqual / LessThanEqual: 이상 / 이하
     * Between: 범위 검색
     * */
    List<Menu> findByOrderableStatusOrderByMenuPriceAscMenuCodeAsc(String orderableStatus);

    // 조건필드 MenuPrice + 비교 키워드 GreaterThanEqual(>=) + 정렬(가격 오름차순, 같으면 메뉴코드 오름차순)
    // → WHERE m.menuPrice >= ?1 ORDER BY m.menuPrice ASC, m.menuCode ASC
    List<Menu> findByMenuPriceGreaterThanEqualOrderByMenuPriceAscMenuCodeAsc(Integer menuPrice);

    // 조건을 줄 땐 And로 연결
    List<Menu> findByOrderableStatusAndMenuPriceLessThanEqualOrderByMenuPriceAscMenuCodeAsc
            (String orderableStatus, Integer menuPrice);

    // 요청한 카테고리에 속한 메뉴를 조회, fk로 찾고 있음
    List<Menu> findByCategory_CategoryCodeOrderByMenuCodeAsc(Integer categoryCode);

    // fk가 아닌 카테고리 이름으로 조회
    List<Menu> findByCategory_CategoryNameOrderByMenuCodeAsc(String categoryName);

    /* _ : 연관관계의 경계를 명확히 표현하기 위해 쓰는 의미*/

    // 메뉴 이름에 검색어가 포함된 메뉴를 조회
    // 조건필드 MenuName + 비교 키워드 Containing(LIKE %검색어%) + 정렬(메뉴코드 오름차순)
    // → WHERE m.menuName LIKE %?1% ORDER BY m.menuCode ASC
    List<Menu> findByMenuNameContainingOrderByMenuCodeAsc(String keyword);
}
