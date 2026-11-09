package org.example.springdatajpa.controller;

import lombok.RequiredArgsConstructor;
import org.example.springdatajpa.dto.MenuResponseDTO;
import org.example.springdatajpa.repository.MenuRepository;
import org.example.springdatajpa.service.MenuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menus/query-method")
@RequiredArgsConstructor
public class QueryMethodMenuController {

    private final MenuService menuService;

    /*
     * 요청한 주문 가능 상태와 일치하는 메뉴를 조회
     * - 가격이 낮은 메뉴부터 반환, 가격이 같으면 메뉴 코드순으로 응답
     * */
    @GetMapping("/orderable")
    public List<MenuResponseDTO> findByOrderableStatus(@RequestParam String status) {
        // 리스펀스 디티오 형태로 만들거임
        return menuService.findByOrderableStatus(status);
    }
}
