package org.example.jpaintro.service;


import lombok.RequiredArgsConstructor;
import org.example.jpaintro.dto.MenuRequestDTO;
import org.example.jpaintro.dto.MenuResponseDTO;
import org.example.jpaintro.entity.Category;
import org.example.jpaintro.entity.Menu;
import org.example.jpaintro.repository.MenuCategoryRepository;
import org.example.jpaintro.repository.MenuRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service // 빈을 등록하자
@RequiredArgsConstructor // final 필드(menuRepository)를 받는 생성자 자동 생성
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuCategoryRepository menuCategoryRepository;

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
    public MenuResponseDTO create(@NonNull MenuRequestDTO request) {
        Category category = menuCategoryRepository.findById(request.getCategoryCode())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."));
        Menu menu = new Menu(
                request.getMenuName(),
                request.getMenuPrice(),
                category,
                request.getOrderableStatus()
        );

        // db에 저장!
        Menu savedMenu = menuRepository.save(menu);

        return MenuResponseDTO.from(savedMenu);
    }

    // update 메서드
    @Transactional
    public MenuResponseDTO update(Integer menuCode, MenuRequestDTO request) {
        Category category = menuCategoryRepository.findById(request.getCategoryCode())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."));

        // id로 수정할 메뉴 정보 찾아오기
        Menu menu = menuRepository.findById(menuCode)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "메뉴를 찾을 수 없습니다."));

        // 값 변경
        menu.change(
                request.getMenuName(),
                request.getMenuPrice(),
                category,
                request.getOrderableStatus());

        return MenuResponseDTO.from(menu);
    }

    //remove 메서드
    @Transactional
    public void remove(Integer menuCode) {
        Menu menu = menuRepository.findById(menuCode)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "메뉴를 찾을 수 없습니다."));

        menuRepository.delete(menu);
    }

    @Transactional
    public void removeByCategory(Integer categoryCode) {
        Category category = menuCategoryRepository.findById(categoryCode)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."));

        List<Menu> menus = menuRepository.findByCategory(category);

        System.out.println(menus);

    }
}
