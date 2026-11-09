package org.example.springdatajpa.service;

import lombok.RequiredArgsConstructor;
import org.example.springdatajpa.dto.MenuRequestDTO;
import org.example.springdatajpa.dto.MenuResponseDTO;
import org.example.springdatajpa.entity.Category;
import org.example.springdatajpa.entity.Menu;
import org.example.springdatajpa.repository.CategoryRepository;
import org.example.springdatajpa.repository.MenuRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<MenuResponseDTO> findAll() {
        return menuRepository.findAllWithCategory().stream()
                .map(MenuResponseDTO::from)
                .toList();
    }

    @Transactional
    public MenuResponseDTO create(MenuRequestDTO request) {
        Category category = findCategory(request.getCategoryCode());
        Menu menu = new Menu(
                request.getMenuName(),
                request.getMenuPrice(),
                category,
                request.getOrderableStatus()
        );

        Menu savedMenu = menuRepository.save(menu);
        return MenuResponseDTO.from(savedMenu);
    }

    @Transactional
    public MenuResponseDTO update(Integer menuCode, MenuRequestDTO request) {
        Menu menu = menuRepository.findById(menuCode)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "메뉴를 찾을 수 없습니다. menuCode=" + menuCode
                ));
        Category category = findCategory(request.getCategoryCode());

        menu.change(
                request.getMenuName(),
                request.getMenuPrice(),
                category,
                request.getOrderableStatus()
        );

        return MenuResponseDTO.from(menu);
    }

    private Category findCategory(Integer categoryCode) {
        return categoryRepository.findById(categoryCode)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "카테고리를 찾을 수 없습니다. categoryCode=" + categoryCode
                ));
    }
}
