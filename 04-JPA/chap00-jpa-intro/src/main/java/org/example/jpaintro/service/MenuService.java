package org.example.jpaintro.service;


import lombok.RequiredArgsConstructor;
import org.example.jpaintro.dto.MenuRequestDTO;
import org.example.jpaintro.dto.MenuResponseDTO;
import org.example.jpaintro.entity.Menu;
import org.example.jpaintro.repository.MenuRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service // 빈을 등록하자
@RequiredArgsConstructor // final 필드(menuRepository)를 받는 생성자 자동 생성
public class MenuService {

    private final MenuRepository menuRepository;

    public List<MenuResponseDTO> findAll() {

        // 데이터베이스에서 조회가됨!
        List<Menu> menuList = menuRepository.findAll();

        // Entity → DTO 변환
        List<MenuResponseDTO> list = menuList.stream()
                .map(menu -> MenuResponseDTO.from(menu))
                .toList();

        return list;
    }

    // create 메서드
    @Transactional
    public MenuResponseDTO create(MenuRequestDTO request) {
        Menu menu = new Menu(
                request.getMenuName(),
                request.getMenuPrice(),
                request.getCategoryCode(),
                request.getOrderableStatus()
        );

        // db에 저장!
        Menu savedMenu = menuRepository.save(menu);

        return MenuResponseDTO.from(savedMenu);
    }

    // update 메서드
    @Transactional
    public MenuResponseDTO update(Integer menuCode, MenuRequestDTO request) {

        // id로 수정할 메뉴 정보 찾아오기
        Menu menu = menuRepository.findById(menuCode)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "메뉴를 찾을 수 없습니다."));

        // 값 변경
        menu.change(
                request.getMenuName(),
                request.getMenuPrice(),
                request.getCategoryCode(),
                request.getOrderableStatus());

        return MenuResponseDTO.from(menu);
    }
}
