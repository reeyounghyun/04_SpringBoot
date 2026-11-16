package org.example.springdatajpa.controller;

import lombok.RequiredArgsConstructor;
import org.example.springdatajpa.dto.MenuRequestDTO;
import org.example.springdatajpa.dto.MenuResponseDTO;
import org.example.springdatajpa.service.MenuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus/query-method")
@RequiredArgsConstructor
public class QueryMethodMenuController {
    private final MenuService menuService;

    // 기본 저장 쿼리메서드
    // menu 저장
    @PostMapping
    public MenuResponseDTO create(@RequestBody MenuRequestDTO menuRequestDTO) {
        return menuService.create(menuRequestDTO);
    }

    // 기본 수정 쿼리메서드
    // menu 수정
    @PutMapping("/{menuCode}")
    public MenuResponseDTO update(@PathVariable Integer menuCode, @RequestBody MenuRequestDTO menuRequestDTO) {
        return menuService.update(menuCode, menuRequestDTO);
    }


    /*
     * 요청한 주문 가능 상태와 일치하는 메뉴를 조회
     * - 가격이 낮은 메뉴부터 반환, 가격이 같으면 메뉴 코드순으로 응답
     * */
    @GetMapping("/orderable")
    public List<MenuResponseDTO> findByOrderableStatus(@RequestParam String status) {
        // MenuResponseDTO 형태로 반환한다
        return menuService.findByOrderableStatus(status);
    }

    /*
     * - 요청 가격 이상인 메뉴 조회
     * - 가격이 낮은 메뉴부터 반환하고, 가격이 같으면 메뉴 코드순으로 반환
     * */
    @GetMapping("/price")
    public List<MenuResponseDTO> findByMinimumPrice(@RequestParam Integer price) {
        // MenuResponseDTO 형태로 반환한다
        return menuService.findByMinimumPrice(price);
    }

    /*
     * - 주문 가능 상태가 요청 상태와 같고, 요청 가격 이하인 메뉴를 조회
     * - 두 조건을 만족하는 메뉴만 가격 오름차순으로 정렬
     * */
    // 여러 조건을 결합
    @GetMapping("/search")
    public List<MenuResponseDTO> findByStatusAndMaximumPrice(
            @RequestParam Integer price,
            @RequestParam String status
    ) {
        return menuService.findByStatusAndMaximumPrice(status, price);
    }


    /*
     * - 요청한 카테고리에 속한 메뉴를 조회
     * - 조회 결과는 메뉴 코드 오름차순
     * */
    @GetMapping("/categories/{categoryCode}")
    public List<MenuResponseDTO> findByCategory(
            @PathVariable Integer categoryCode
    ) {
        return menuService.findByCategoryQueryMethod(categoryCode);
    }



    /*
    * fk가 아닌 카테고리 이름으로 조회
    * */
    @GetMapping("/category-name")
    public List<MenuResponseDTO> findByCategoryName(
            @RequestParam String categoryName
    ) {
        return menuService.findByCategoryNameQueryMethod(categoryName);
    }

    /*
    * - 메뉴 이름에 요청한 검색어가 포함된 메뉴를 조회
    * */
    @GetMapping("/menu-name")
    public List<MenuResponseDTO> findByMenuName(
            @RequestParam String keyword
    ) {
        return menuService.findByMenuNameQueryMethod(keyword);
    }

}
