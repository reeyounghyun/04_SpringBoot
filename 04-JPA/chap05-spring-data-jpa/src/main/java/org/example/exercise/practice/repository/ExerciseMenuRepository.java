package org.example.exercise.practice.repository;

import org.example.springdatajpa.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * ===============================================================
 *  [연습문제] Part 3. Repository 작성
 *  - 각 문제 아래에 @Query와 메서드를 작성한다.
 *  - 주의: 쿼리가 비어 있는 @Query를 남겨두면 앱이 기동되지 않는다.
 *          완성한 문제부터 하나씩 작성할 것!
 * ===============================================================
 */
public interface ExerciseMenuRepository extends JpaRepository<Menu, Integer> {

    /*
     * [3-1] 쿼리 메서드
     * - 메뉴 이름이 특정 단어로 "시작하는" 메뉴를 조회
     * - 가격 내림차순 정렬
     * - 매개변수: String word
     */


    /*
     * [3-2] JPQL + LIKE
     * - 메뉴 이름에 키워드가 "포함"되고
     * - 주문 가능 상태(orderableStatus)가 "Y"인 메뉴를 조회
     * - 메뉴 코드 오름차순 정렬
     * - 메서드: List<Menu> searchOrderableByName(@Param("keyword") String keyword);
     */


    /*
     * [3-3] JPQL 서브쿼리
     * - "전체 메뉴"의 평균 가격보다 싼 메뉴를 조회
     * - 가격 오름차순 정렬
     * - 힌트: 실습에서는 카테고리별 평균이었고, 이번에는 전체 평균이다.
     * - 메서드: List<Menu> findMenusBelowAverage();
     */


    /*
     * [3-4] 벌크 수정 (@Modifying)
     * - 지정한 카테고리의 모든 메뉴를 주문 불가("N")로 일괄 변경
     * - 변경된 개수를 반환
     * - @Modifying 옵션(flushAutomatically, clearAutomatically)도 포함
     * - 메서드: int disableByCategory(@Param("category") Category category);
     */


    /*
     * [3-5] 네이티브 쿼리
     * - 가격이 minPrice 이상인 메뉴를 조회
     * - 테이블: tbl_menu / 컬럼: menu_price, menu_code
     * - 메뉴 코드 오름차순 정렬
     * - 메서드: List<Menu> findByMinPriceNative(@Param("minPrice") Integer minPrice);
     */


    /*
     * [4-1] 응용 - 카테고리별 메뉴 가격 일괄 인하 (Repository 부분)
     * - 할인된 가격 = 가격 - (가격 * 할인율 / 100)
     * - 지정한 카테고리의 메뉴만 수정, 변경된 개수를 반환
     * - 메서드: int discountPriceByCategory(Category category, Integer discountRate);
     * - Controller → Service → Repository 순서로 작성 (Controller, Service 파일에도 문제 있음)
     */

}
