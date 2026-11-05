package org.example.jpaintro.controller;

import lombok.RequiredArgsConstructor;
import org.example.jpaintro.dto.MenuRequestDTO;
import org.example.jpaintro.dto.MenuResponseDTO;
import org.example.jpaintro.service.MenuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * [Controller 계층] HTTP 요청을 받는 입구
 *
 * - 클라이언트의 요청(/api/menus)을 받아 Service 에 처리를 위임한다.
 * - 요청 데이터는 MenuRequestDTO 로 받고, 응답은 MenuResponseDTO 로 돌려준다.
 * - 비즈니스 로직은 직접 처리하지 않는다. (Service 의 역할)
 *
 * 흐름: 클라이언트 → [Controller] → Service → Repository → DB
 */


@RestController                 // @Controller + @ResponseBody : 반환값을 JSON 으로 응답
@RequestMapping("/api/menus")   // 이 컨트롤러의 공통 URL
@RequiredArgsConstructor // 초기화가 필요한 필드만 생성자로 만들어주는 어노테이션
public class MenuController {

    /* 생성자 주입 : @RequiredArgsConstructor 가 만든 생성자로 스프링이 MenuService 빈을 넣어준다. (MenuService 에 @Service 필요) */
    private final MenuService menuService;

    // 전체조회
    @GetMapping
    public List<MenuResponseDTO> findAll() {
        return menuService.findAll();
    }

    // 메뉴 저장
    @PostMapping
    public MenuResponseDTO create(@RequestBody MenuRequestDTO menuRequestDTO) {
        return menuService.create(menuRequestDTO);
    }

    @PutMapping("/{menuCode}")
    public MenuResponseDTO update(@PathVariable Integer menuCode, @RequestBody MenuRequestDTO menuRequestDTO) {
        return menuService.update(menuCode, menuRequestDTO);
    }
}
