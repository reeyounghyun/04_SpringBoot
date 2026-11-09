package org.example.springdatajpa.controller;

import lombok.RequiredArgsConstructor;
import org.example.springdatajpa.dto.MenuResponseDTO;
import org.example.springdatajpa.service.MenuService;
import org.springframework.web.bind.annotation.GetMapping;
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