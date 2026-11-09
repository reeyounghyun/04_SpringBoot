package org.example.springdatajpa.controller;

import lombok.RequiredArgsConstructor;
import org.example.springdatajpa.dto.MenuRequestDTO;
import org.example.springdatajpa.dto.MenuResponseDTO;
import org.example.springdatajpa.service.MenuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor // 초기화가 필요한 필드만 생성자로 만들어주는 어노테이션
public class MenuController {

    private final MenuService menuService;

    // 전체조회
    @GetMapping
    public List<MenuResponseDTO> findAll() {

        return menuService.findAll();
    }

    // menu 저장
    @PostMapping
    public MenuResponseDTO create(@RequestBody MenuRequestDTO menuRequestDTO) {

        return menuService.create(menuRequestDTO);
    }

    @PutMapping("/{menuCode}")
    public MenuResponseDTO update(@PathVariable Integer menuCode, @RequestBody MenuRequestDTO menuRequestDTO) {

        return menuService.update(menuCode,menuRequestDTO);
    }
}
