package org.example.exercise.practice.controller;

import lombok.RequiredArgsConstructor;
import org.example.exercise.practice.service.PracticeMenuService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * ===============================================================
 *  [연습문제] Controller 작성
 *  - 공통 경로: /api/practice/menus
 *  - 작성 순서: Controller → Service → Repository
 *  - 아래 API 명세대로 메서드를 작성한다.
 * ===============================================================
 */
@RestController
@RequestMapping("/api/practice/menus")
@RequiredArgsConstructor
public class PracticeMenuController {
    private final PracticeMenuService practiceMenuService;

    /*
     * [3-1] 이름이 특정 단어로 시작하는 메뉴 조회 (쿼리 메서드)
     * - GET /api/practice/menus/starts-with?word=김
     * - 응답: List<MenuResponseDTO>
     */


    /*
     * [3-2] 이름에 키워드가 포함된 주문 가능 메뉴 조회 (JPQL + LIKE)
     * - GET /api/practice/menus/orderable/search?keyword=찌개
     * - 응답: List<MenuResponseDTO>
     */


    /*
     * [3-3] 전체 평균 가격보다 싼 메뉴 조회 (JPQL 서브쿼리)
     * - GET /api/practice/menus/below-average
     * - 응답: List<MenuResponseDTO>
     */


    /*
     * [3-4] 카테고리의 모든 메뉴를 주문 불가로 일괄 변경 (@Modifying)
     * - PATCH /api/practice/menus/categories/{categoryCode}/disable
     * - 응답: 변경된 개수 (int)
     */


    /*
     * [3-5] 가격이 minPrice 이상인 메뉴 조회 (네이티브 쿼리)
     * - GET /api/practice/menus/native/price?minPrice=10000
     * - 응답: List<MenuResponseDTO>
     */


    /*
     * [4-1] 카테고리별 메뉴 가격 일괄 인하 (응용)
     * - PATCH /api/practice/menus/categories/{categoryCode}/discount
     * - 요청: { "discountRate": 20 }   → DiscountRequestDTO
     * - 응답: { "updatedCount": 3 }    → DiscountResponseDTO
     */

}
