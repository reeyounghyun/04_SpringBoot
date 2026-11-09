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
import java.util.stream.Collectors;

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

    /*
     * 주문 가능 상태(status)와 일치하는 메뉴를 조회해서 응답용 DTO 목록으로 돌려준다.
     * - 컨트롤러에서 받은 status("Y" 또는 "N")를 그대로 Repository 쿼리 메서드에 넘긴다.
     * - 정렬(가격 오름차순 → 메뉴코드 오름차순)은 쿼리 메서드 이름의 OrderBy 부분이 DB에서 처리한다.
     */
    public List<MenuResponseDTO> findByOrderableStatus(String status) {
        // 1. 쿼리 메서드 호출
        //    → SELECT m FROM Menu m WHERE m.orderableStatus = :status ORDER BY m.menuPrice ASC, m.menuCode ASC
        //    결과는 엔티티(Menu) 목록이다.
        List<Menu> menuList =
                menuRepository.findByOrderableStatusOrderByMenuPriceAscMenuCodeAsc(status);

        // 2. 엔티티(Menu) → 응답 DTO(MenuResponseDTO)로 변환
        //    stream()  : 리스트의 요소를 하나씩 흘려보낸다.
        //    map(...)  : 흘러가는 Menu 하나하나를 MenuResponseDTO.from(menu)로 바꾼다.
        //    toList()  : 바뀐 결과를 다시 리스트로 모은다.
        //    엔티티를 그대로 응답하지 않고 DTO로 바꾸는 이유: 필요한 값만 내보내고, 엔티티 구조가 바뀌어도 API 응답은 지키기 위해
        List<MenuResponseDTO>  listMenuResponseDto = menuList.stream()
                .map(menu -> MenuResponseDTO.from(menu))
                .toList();

        // 3. 변환된 DTO 목록을 컨트롤러로 반환 → JSON 배열로 응답된다.
        return listMenuResponseDto;
    }
}
