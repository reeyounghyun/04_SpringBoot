package org.example.springdatajpa.repository;

import org.example.springdatajpa.dto.CategoryMenuCountProjection;
import org.example.springdatajpa.dto.CategoryMenuStatisticsDTO;
import org.example.springdatajpa.dto.OrderableMenuCountProjection;
import org.example.springdatajpa.dto.findCategoryPriceStatisticsDTO;
import org.example.springdatajpa.entity.Category;
import org.example.springdatajpa.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Integer> {

    /* 네이티브 쿼리보다 JPQL이 권장되는 이유?
     * 1. DB종류에 덜 의존한다.
     * 2. JPA 기능과 자연스럽게 연결된다 -> 엔티티 기준으로 사용 가능하다.
     * 3. 네이티브 쿼리 -> 데이터베이스 테이블명, 컬럼명에 의존하지만, JPQL은 엔티티 구조로 관리 가능
     *    (테이블명·컬럼명이 바뀌어도 엔티티의 @Table, @Column 매핑만 수정하면 JPQL은 그대로 사용할 수 있다)
     * 네이티브 쿼리는 -> 해당 데이터베이스의 특징 기능
     * */

    // insert update delete 단순 기존데이터만 가져올 수 있고 집계를 할수도 있다
    // -> 안에서 응답받는 데이터가(엔티티 필드의 전체, 혹은 일부)
    // -> 엔티티 필드가 아닌 데이터 > 엔티티에 필드로 넣어야하나? 고민이 생김
    //    (쉽게 말해) 집계 결과(COUNT, AVG 등)처럼 엔티티에 없는 값을 조회하면 어디에 담아야 할지 고민이 생김
    //    엔티티에 필드를 추가하면 테이블 매핑이 깨지므로 X
    // -> 해결방법 프로젝션을 사용할거임.(이전에 했던 방식으로 인터페이스로 결과 클래스를 만드는 것과 동일함)
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

    /*
     * DTO Projection
     * - JPQL에서 new 키워드와 DTO의 전체 패키지 이름을 사용
     *
     * 주의점
     * - JPQL에 DTO의 전체 패키지 이름을 작성
     * - select 항목의 순서와 DTO 생성자 매개변수 순서가 같아야함.
     * - count 결과는 Long, AVG 결과는 Double
     * - 생성자 필요!
     *
     * 순서가 중요하다! (select 항목 → DTO 생성자 매개변수에 순서대로 들어감)
     *   1번째 category.categoryCode  → Integer categoryCode
     *   2번째 category.categoryName  → String  categoryName
     *   3번째 count(menu)            → Long    menuCount
     *   4번째 avg(menu.menuPrice)    → Double  averagePrice
     *   순서가 바뀌면 타입이 맞는 생성자를 찾지 못해 앱 실행 시 에러가 나고,
     *   타입이 같은 값끼리 바뀌면 에러 없이 엉뚱한 필드에 값이 들어간다.
     * */
    @Query("""
            select new org.example.springdatajpa.dto.CategoryMenuStatisticsDTO(
                        category.categoryCode,
                        category.categoryName,
                        count(menu),
                        avg(menu.menuPrice)
                    )
            from Menu menu
            join menu.category category
            group by category.categoryCode, category.categoryName
            order by category.categoryCode asc
            """)
    List<CategoryMenuStatisticsDTO> findCategoryStatistics();

    /*
     * 인터페이스 Projection
     * - new 키워드와 패키지 이름이 필요 없다.
     * - select 항목마다 as 별칭을 붙이고, 별칭 = 인터페이스 getter 이름(get 뒤 부분)으로 맞춘다.
     */
    @Query("""
            select category.categoryCode as categoryCode,
                   category.categoryName as categoryName,
                   count(menu) as menuCount
            from Menu menu
            join menu.category category
            group by category.categoryCode, category.categoryName
            order by category.categoryCode asc
            """)
    List<CategoryMenuCountProjection> findCategoryMenuCounts();

    @Query("""
            select menu
            from Menu menu
            join menu.category
            where menu.category.categoryCode = :categoryCode
            order by menu.menuCode asc
            """)
    List<Menu> findByCategory(Integer categoryCode);

    /*
     * 가격 범위 + 주문 가능 상태 검색
     * - between A and B : A 이상 B 이하 (양 끝 포함)
     * - :이름 → 메서드 매개변수 이름과 같은 값이 바인딩된다.
     */
    @Query("""
            select menu
            from Menu menu
            join menu.category
            where menu.menuPrice between :minPrice and :maxPrice
            and menu.orderableStatus = :status
            order by menu.menuPrice asc, menu.menuCode asc
            """)

    // @Param JPQL 파라미터와 Java 매개변수의 연결을 명확히 하기 위해 사용(생략 가능) ->  @Param이 JPQL 쿼리 안의 :이름 파라미터와 Java 메서드의 매개변수를 연결해 준다
    List<Menu> searchByPriceRange(
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("status") String status);


    /*메뉴이름에 요청한 검색어가 포함된 메뉴 조회*/
    @Query("""
            select menu
            from Menu menu
            join menu.category
            where menu.menuName like concat('%', :keyword, '%') 
            order by menu.menuCode asc
            """)
    List<Menu> searchByName(@Param("keyword") String keyword);

    /*
     * JPQL 서브쿼리
     * - 각 메뉴를 자신이 속한 카테고리의 평균 가격과 비교
     * - 괄호 안(서브쿼리)은 바깥 메뉴(menu)와 같은 카테고리인 메뉴(subMenu)들의 평균 가격을 구한다.
     * - join fetch : 카테고리를 함께 조회해 DTO 변환 시 추가 쿼리(N+1)가 나가지 않게 한다.
     */
    @Query("""
            select menu
            from Menu menu
            join fetch menu.category
            where menu.menuPrice > (
                        select avg(subMenu.menuPrice)
                        from Menu subMenu
                        where subMenu.category = menu.category
                        )
            order by menu.category.categoryCode asc
            """)
    List<Menu> findMenusAboveCategoryAverage();

    /*
     * @Modifying
     * - @Query : 기본적으로 조회용 select 쿼리로 처리된다.
     * - @Query에 작성된 쿼리가 조회가 아니라 데이터를 변경하는 Insert, Update, Delete 성격의 쿼리라는 사실을 JPA에게 알려준다.
     * - JPQL 또는 네이티브 쿼리로 Update, Delete를 실행할 때
     * */

    /*
     * flushAutomatically = true
     *  - 벌크쿼리(많은 동일한 작업을 한번에)를 실행하기 전에 영속성 컨텍스트의 변경 내용을 DB에 Flush
     * clearAutomatically = true
     *  - 벌크쿼리를 실행한 후에, 영속성 컨텍스트를 자동으로 초기화한다.
     *  - 벌크쿼리는 영속성 컨텍스트를 거치지 않고 DB를 직접 수정하므로, 초기화하지 않으면 이전 값이 남아 있는 엔티티를 조회할 수 있다.
     * */
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update Menu menu
            set menu.menuPrice = menu.menuPrice + (menu.menuPrice * :increaseRate / 100)
            where menu.category = :category
            """)
    int increasePriceByCategory(Category category, Integer increaseRate);


    // nativeQuery = true : 일반 DB 쿼리를 쓸 수 있게 해줌
    @Query(value = """
            select menu.*
            from tbl_menu menu
            where menu.category_code = :categoryCode
            order by menu.menu_code asc
            """, nativeQuery = true)
    List<Menu> findByCategoryNative(Integer categoryCode);


    /* [기본 실습 1] */
    List<Menu> findByMenuPriceBetweenOrderByMenuPriceDescMenuCodeAsc(Integer minPrice, Integer maxPrice);

    /* [기본 실습 2] */
    List<Menu> findByCategoryCategoryCodeAndOrderableStatus(Integer categoryCode, String status);

    /* [기본 실습 3]*/

    @Query("""
        select menu
        from Menu menu
        join fetch menu.category
        where menu.orderableStatus = 'Y'
        order by menu.menuPrice asc, menu.menuCode asc
        """)
    List<Menu> findOrderableMenus();


    /*[기본 실습 4]*/
    @Query("""
        select menu
        from Menu menu
        join fetch menu.category
        where menu.category.categoryCode = :categoryCode
        and menu.menuPrice <= :maxPrice
        order by menu.menuPrice asc, menu.menuCode asc
        """)
    List<Menu> searchMenus(
            @Param("categoryCode") Integer categoryCode,
            @Param("maxPrice") Integer maxPrice);



    /* [기본 실습 5]*/
    @Query("""
        select category.categoryCode as categoryCode,
               category.categoryName as categoryName,
               count(menu) as menuCount
        from Menu menu
        join menu.category category
        where menu.orderableStatus = 'Y'
        group by category.categoryCode, category.categoryName
        order by category.categoryCode asc
        """)
    List<OrderableMenuCountProjection> findOrderableMenuCounts();


    /* [기본 실습 6]*/
    @Query("""
      select new org.example.springdatajpa.dto.findCategoryPriceStatisticsDTO(
      category.categoryCode,
      category.categoryName,
      min(menu.menuPrice),
      max(menu.menuPrice)
     )
       from Menu menu
       join menu.category category
       group by category.categoryCode, category.categoryName
       order by category.categoryCode asc
    """)
    List<findCategoryPriceStatisticsDTO> findCategoryPriceStatistics();


    /* [기본 실습 7] */
    @Query("""
        select menu
        from Menu menu
        join fetch menu.category
        where menu.menuPrice < (
           select avg(m.menuPrice)
           from Menu m
          )
        order by menu.menuPrice asc, menu.menuCode asc
        """)
    List<Menu> findMenusBelowAverage();


    /* [기본 실습 8] */
    @Query(value = """
        select menu.*
        from tbl_menu menu
        where menu.menu_price >= :price
        order by menu.menu_price desc, menu.menu_code asc
        """, nativeQuery = true)
    List<Menu> findExpensiveMenusNative(@Param("price") Integer price);
}






