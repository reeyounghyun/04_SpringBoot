package org.example.jpaintro.controller;

import org.example.jpaintro.service.MenuService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class MenuController {

    private final MenuService menuService;

    /* 생성자 주입 : 스프링이 MenuService 빈을 찾아 넣어준다. (MenuService 에 @Service 필요) */
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }
}
