package org.example.springdatajpa.controller;

import lombok.RequiredArgsConstructor;
import org.example.springdatajpa.dto.MenuResponseDTO;
import org.example.springdatajpa.dto.PriceIncreaseRequestDTO;
import org.example.springdatajpa.dto.PriceUpdateResponseDTO;
import org.example.springdatajpa.service.MenuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class JpqlMenuController {
    private final MenuService menuService;

    // 전체조회
    @GetMapping
    public List<MenuResponseDTO> findAll() {
        return menuService.findAll();
    }

    // 요청한 카테고리에 속한 메뉴 조회 (JPQL)
    @GetMapping("/categories/{categoryCode}")
    public List<MenuResponseDTO> findByCategory(@PathVariable Integer categoryCode) {
        return menuService.findByCategory(categoryCode);
    }

    /*
     * 가격 범위와 주문 가능 상태 검색
     * - 지정한 가격 범위에 포함되고 주문 가능 상태가 일치하는 메뉴 조회
     * */
    @GetMapping("/search")
    public List<MenuResponseDTO> searchByPriceRange(
            @RequestParam Integer minPrice,
            @RequestParam Integer maxPrice,
            @RequestParam String status
    ) {
        return menuService.searchByPriceRange(minPrice, maxPrice, status);
    }

    /*
     * 메뉴이름에 요청한 검색어가 포함된 메뉴를 조회한다.
     * JPQL로 진행
     * */
    @GetMapping("/search/name")
    public List<MenuResponseDTO> searchByName(@RequestParam String keyword) {
        return menuService.searchByName(keyword);
    }


    /* JPQL 서브쿼리
     * 각 메뉴를 자신이 속한 카테고리의 평균 가격과 비교
     * 해당 평균보다 비싼 메뉴만 카테고리와 함께 조회*/
    @GetMapping("/above-category-average")
    public List<MenuResponseDTO> findMenusAboveCategoryAverage() {
        return menuService.findMenusAboveCategoryAverage();
    }

    /*
     * PATCH VS Put
     * - PATCH는 리소스 전체가 아니라 일부 속성이나 특정 상태만 변경할때 -일부분 만 (카테고리는 유지하고, 가격만 변경할 때)
     * - PUT은 리소스 전체를 요청 본문의 내용으로 교체할 때 사용 - 전체
     *
     * 멱등성
     - PATCH 설계에 따라 멱등일 수도 아닐 수도 있음
     - PUT 요청을 여러번 보내도 일반적으로 최종 상태는 같아야함.
     */

    // 카테고리별 메뉴 가격 일괄 인상
    // PATCH
    // 지정한 카테고리의 모든 메뉴 가격을 요청 비용만큼 한번에 인상
    // 변경된 갯수를 반환받기
    @PatchMapping("categories/{categoryCode}/price")
    public PriceUpdateResponseDTO increasePriceByCategory(
            @PathVariable Integer categoryCode,
            @RequestBody PriceIncreaseRequestDTO request
    ) {
        return menuService.increasePriceByCategory(categoryCode, request.getIncreaseRate());
    }
}


// 코드 작성 순서
/*
 *
 * 1. Controller 작성:
 *      1.어노테이션
 *          - 조회 메서드는 @Transactional(readOnly = true),
 *          - 등록·수정·삭제 메서드는 @Transactional을 붙입니다.
 *      2. 요청할 API,
 *      3. 돌려받을 변수
 *
 * 2. Service 작성
 * 3. Repository 작성
 * */