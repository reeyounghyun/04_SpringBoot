package org.example.springdatajpa.controller;

import lombok.RequiredArgsConstructor;
import org.example.springdatajpa.dto.MenuResponseDTO;
import org.example.springdatajpa.entity.Menu;
import org.example.springdatajpa.service.MenuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
* NativeQuery
*  사용하는 데이터베이스의 SQL을 직접 작성하는 방식 이다.
* */
@RestController
@RequestMapping("/api/menus/native/")
@RequiredArgsConstructor
public class NativeQueryMenuController {
    private final MenuService menuService;

    // 요청한 카테고리에 속한 메뉴를 실제 테이블 명과 컬럼으로 조회한다.
    @GetMapping("/categories/{categoryCode}")
    public List<MenuResponseDTO> findByCategoryNative(@PathVariable Integer categoryCode) {
        return menuService.findByCategoryNative(categoryCode);
    }
}
