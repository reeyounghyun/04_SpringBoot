package org.example.springdatajpa.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.springdatajpa.dto.MenuResponseDTO;
import org.example.springdatajpa.service.MenuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/practice")
@RequiredArgsConstructor
@Tag(name = "90. 기본 실습 문제", description = "쿼리 메서드부터 기본 Native Query까지 구현하는 문제")
public class PracticeMenuController {

    private final MenuService menuService;

    /*
     * [기본 실습 1] 가격 범위 메뉴 조회
     * GET /api/practice/menus/query-method/price-range?minPrice=5000&maxPrice=10000
     *
     * 요구사항
     * - 요청 가격 범위에 포함되는 메뉴를 쿼리 메서드로 조회한다.
     * - 가격 내림차순으로 반환하고, 같은 가격은 메뉴 코드순으로 정렬한다.
     * - 잘못된 가격 범위는 400 BAD_REQUEST로 처리한다.
     */

    @GetMapping("/menus/query-method/price-range")
    @Operation(summary = "기본 01. 가격 범위 쿼리 메서드", description = "쿼리 메서드로 가격 범위 메뉴를 조회하는 문제입니다.")
    public List<MenuResponseDTO> findMenusByPriceRange(
            @RequestParam Integer minPrice,
            @RequestParam Integer maxPrice
    ) {
        return menuService.findMenusByPriceRange(minPrice, maxPrice);
    };

    /*
     * [기본 실습 2] 카테고리와 주문 상태 조회
     * GET /api/practice/menus/query-method/categories/{categoryCode}?status=Y
     *
     * 요구사항
     * - 카테고리와 주문 가능 상태를 모두 만족하는 메뉴를 쿼리 메서드로 조회한다.
     * - 연관된 Category의 필드를 조건으로 사용한다.
     * - 카테고리가 없으면 404 NOT_FOUND를 반환한다.
     */
    @GetMapping("/menus/query-method/categories/{categoryCode}")
    @Operation(summary = "기본 02. 연관관계 쿼리 메서드", description = "카테고리와 주문 상태 조건을 쿼리 메서드로 조회하는 문제입니다.")
    public List<MenuResponseDTO> findMenusByCategoryAndStatus(
            @PathVariable Integer categoryCode,
            @RequestParam String status
    ) {
        return menuService.findMenusByCategoryAndStatus(categoryCode, status);
    }

    /*
     * [기본 실습 3] 주문 가능한 메뉴 조회
     * GET /api/practice/menus/orderable
     *
     * 요구사항
     * - 주문 가능한 메뉴를 JPQL로 조회한다.
     * - 가격이 낮은 메뉴부터 반환한다.
     * - 응답에 카테고리 코드와 이름을 포함한다.
     */
    @GetMapping("/menus/orderable")
    @Operation(summary = "기본 03. 주문 가능 메뉴", description = "JPQL로 주문 가능한 메뉴를 조회하는 문제입니다.")
    public List<?> findOrderableMenus() {
        return menuService.findOrderableMenus();
    }

    /*
     * [기본 실습 4] 카테고리와 최대 가격 검색
     * GET /api/practice/menus/search?categoryCode=4&maxPrice=10000
     *
     * 요구사항
     * - 지정한 카테고리에서 최대 가격 이하인 메뉴를 JPQL로 조회한다.
     * - 카테고리가 없으면 404, 잘못된 가격은 400을 반환한다.
     * - 요청값은 파라미터 바인딩으로 전달한다.
     */
    @GetMapping("/menus/search")
    @Operation(summary = "기본 04. 카테고리·가격 검색", description = "JPQL로 카테고리와 최대 가격 조건을 검색하는 문제입니다.")
    public List<?> searchMenus(
            @RequestParam Integer categoryCode,
            @RequestParam Integer maxPrice
    ) {
        return menuService.searchMenus(categoryCode, maxPrice);
    }

    /*
     * [기본 실습 5] 카테고리별 주문 가능 메뉴 개수
     * GET /api/practice/categories/orderable-menu-counts
     *
     * 요구사항
     * - 카테고리별 주문 가능 메뉴 개수를 집계한다.
     * - 카테고리 코드, 이름과 메뉴 개수를 반환한다.
     * - 인터페이스 Projection을 사용한다.
     */
    @GetMapping("/categories/orderable-menu-counts")
    @Operation(summary = "기본 05. 인터페이스 Projection", description = "카테고리별 주문 가능 메뉴 개수를 조회하는 문제입니다.")
    public List<?> findOrderableMenuCounts() {
        return menuService.findOrderableMenuCounts();
    }

    /*
     * [기본 실습 6] 카테고리별 가격 통계
     * GET /api/practice/categories/price-statistics
     *
     * 요구사항
     * - 카테고리별 최저 가격과 최고 가격을 조회한다.
     * - 카테고리 정보와 계산 결과를 DTO로 반환한다.
     * - JPQL DTO Projection을 사용한다.
     */
    @GetMapping("/categories/price-statistics")
    @Operation(summary = "기본 06. DTO Projection", description = "카테고리별 최저·최고 가격을 DTO로 조회하는 문제입니다.")
    public List<?> findCategoryPriceStatistics() {
        return menuService.findCategoryPriceStatistics();
    }

    /*
     * [기본 실습 7] 전체 평균보다 저렴한 메뉴
     * GET /api/practice/menus/below-average
     *
     * 요구사항
     * - 전체 메뉴 평균 가격보다 저렴한 메뉴를 JPQL로 조회한다.
     * - 평균 계산은 쿼리 안에서 처리한다.
     * - 응답에 카테고리 정보를 포함한다.
     */
    @GetMapping("/menus/below-average")
    @Operation(summary = "기본 07. JPQL 서브쿼리", description = "전체 평균보다 저렴한 메뉴를 조회하는 문제입니다.")
    public List<?> findMenusBelowAverage() {
        return menuService.findMenusBelowAverage();
    }

    /*
     * [기본 실습 8] 네이티브 가격 검색
     * GET /api/practice/menus/native/expensive?price=10000
     *
     * 요구사항
     * - 요청 가격 이상인 메뉴를 네이티브 쿼리로 조회한다.
     * - Projection 없이 Menu 엔티티로 결과를 받는다.
     * - 가격이 높은 메뉴부터 반환한다.
     */
    @GetMapping("/menus/native/expensive")
    @Operation(summary = "기본 08. 기본 네이티브 쿼리", description = "네이티브 SQL로 요청 가격 이상의 메뉴를 조회하는 문제입니다.")
    public List<?> findExpensiveMenusNative(@RequestParam Integer price) {
        return menuService.findExpensiveMenusNative(price);
    }
}