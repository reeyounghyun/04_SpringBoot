package org.example.springdatajpa.service;

import lombok.RequiredArgsConstructor;
import org.example.springdatajpa.dto.*;
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

    /*
    * Transactional(readOnly = true)
    * - 이 트랜잭션이 데이터를 변경하지 않고 조회만 한다는 의도를 Hibernate에게 알려주는 설정
    *
    * 붙였을때의 이점
    * 1. Hibernate가 조회 결과 엔티티의 변경 감지를 위한 작업을 줄일 수 있다.
    * 2. 트랜잭션이 끝났을 때 불필요한 Flush가 발생하지 않도록 함.
    * 3. 코드를 읽는 개발자한테 이 메서드가 조회전용이라는 의도를 전달
    * 4. 메서드가 실행되는동안 영속성 컨텍스트를 유지(여러 조회도 일관된 범위로 처리 가능) -> LAZY도 안전하게 조회됨
    * 주의할점: select를 강제하는 기능은 아니므로 코드 작성시 유의해야함.
    * */
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



    /*
     * 주문 가능 상태(status)와 일치하는 메뉴를 조회해서 응답용 DTO 목록으로 돌려준다.
     * - 컨트롤러에서 받은 status("Y" 또는 "N")를 그대로 Repository 쿼리 메서드에 넘긴다.
     * - 정렬(가격 오름차순 → 메뉴코드 오름차순)은 쿼리 메서드 이름의 OrderBy 부분이 DB에서 처리한다.
     */
    // 요청한 주문 가능 상태와 일치하는 메뉴를 조회
    public List<MenuResponseDTO> findByOrderableStatus(String status) {
        // 1. 쿼리 메서드 호출
        List<Menu> menuList =
                menuRepository.findByOrderableStatusOrderByMenuPriceAscMenuCodeAsc(status);

        // 2. 엔티티(Menu) → 응답 DTO(MenuResponseDTO)로 변환
        //    stream()  : 리스트의 요소를 하나씩 흘려보낸다.
        //    엔티티를 그대로 응답하지 않고 DTO로 바꾸는 이유: 필요한 값만 내보내고, 엔티티 구조가 바뀌어도 API 응답은 지키기 위해
        List<MenuResponseDTO>  listMenuResponseDto = menuList.stream()

                //map(...)  : 흘러가는 Menu 하나하나를 MenuResponseDTO.from(menu)로 바꾼다.
                .map(menu -> MenuResponseDTO.from(menu))

                // toList()  : 바뀐 결과를 다시 리스트로 모은다
                .toList();

        // 3. 변환된 DTO 목록을 컨트롤러로 반환 → JSON 배열로 응답된다.
        return listMenuResponseDto;
    }

    // 요청 가격 이상인 메뉴 조회
    public List<MenuResponseDTO> findByMinimumPrice(Integer price) {

        List<Menu> menuList = menuRepository.findByMenuPriceGreaterThanEqualOrderByMenuPriceAscMenuCodeAsc(price);

        return menuList.stream().map(menu -> MenuResponseDTO.from(menu)).toList();
    }

    // 여러 조건을 결합
    public List<MenuResponseDTO> findByStatusAndMaximumPrice(String status, Integer price) {

        List<Menu> menuList = menuRepository
                .findByOrderableStatusAndMenuPriceLessThanEqualOrderByMenuPriceAscMenuCodeAsc(status, price);

        return menuList.stream().map(MenuResponseDTO::from).toList();
    }

    //요청한 카테고리에 속한 메뉴를 조회
    public List<MenuResponseDTO> findByCategoryQueryMethod(Integer categoryCode) {

        List<Menu> menuList = menuRepository
                .findByCategory_CategoryCodeOrderByMenuCodeAsc(categoryCode);

        return menuList.stream().map(MenuResponseDTO::from).toList();
    }

    // fk가 아닌 카테고리 이름으로 조회
    public List<MenuResponseDTO> findByCategoryNameQueryMethod(String categoryName) {
        List<Menu> menuList = menuRepository
                .findByCategory_CategoryNameOrderByMenuCodeAsc(categoryName);

        return menuList.stream().map(MenuResponseDTO::from).toList();
    }

    // 메뉴 이름에 검색어(keyword)가 포함된 메뉴를 조회
    public List<MenuResponseDTO> findByMenuNameQueryMethod(String keyword) {

        List<Menu> menuList = menuRepository
                .findByMenuNameContainingOrderByMenuCodeAsc(keyword);

        return menuList.stream().map(MenuResponseDTO::from).toList();
    }

    // 클래스안에서만 사용하는 메서드 -> 검증코드
    private Category findCategory(Integer categoryCode) {
        return categoryRepository.findById(categoryCode)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "카테고리를 찾을 수 없습니다. categoryCode=" + categoryCode
                ));
    }

    @Transactional(readOnly = true)
    public List<CategoryMenuStatisticsDTO> findCategoryStatistics() {
        return menuRepository.findCategoryStatistics();
    }

    // 인터페이스 Projection으로 카테고리별 메뉴 개수 조회
    @Transactional(readOnly = true)
    public List<CategoryMenuCountProjection> findCategoryMenuCounts() {
        return menuRepository.findCategoryMenuCounts();
    }

    // JPQL로 요청한 카테고리에 속한 메뉴 조회
    @Transactional(readOnly = true)
    public List<MenuResponseDTO> findByCategory(Integer categoryCode) {
        List<Menu> menuList = menuRepository
                .findByCategory(categoryCode);

        return menuList.stream().map(MenuResponseDTO::from).toList();
    }

    // JPQL로 가격 범위와 주문 가능 상태가 일치하는 메뉴 조회
    @Transactional(readOnly = true)
    public List<MenuResponseDTO> searchByPriceRange(Integer minPrice, Integer maxPrice, String status) {
       // minPrice 또는 maxPrice가 null -> 예외) 가격 범위를 올바르게 입력해야합니다.
       // minPrice < 0, maxPrice < 0 -> 예외) 가격 범위를 올바르게 입력해야합니다.
       // minPrice > maxPrice -> 예외) 가격 범위를 올바르게 입력해야합니다.
       // ResponseStatusException 상품코드에 있는 예외를 사용하려고 사용함.

        //[조건 1]
        if(minPrice == null || maxPrice == null || minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "가격 범위를 올바르게 입력해야합니다.");
        }
        //[조건 2 ] status -> Y or N
        if(!"Y".equals(status) && !"N".equals(status)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 가능 상태는 Y 또는 N 이여야 합니다.");
        }

        List<Menu> menuList = menuRepository
                .searchByPriceRange(minPrice, maxPrice, status);

        return menuList.stream().map(MenuResponseDTO::from).toList();
    }

    // 메뉴이름에 요청한 검색어가 포함된 메뉴 조회
    @Transactional(readOnly = true)
    public List<MenuResponseDTO> searchByName(String keyword) {
        List<Menu> menuList = menuRepository
                .searchByName(keyword);

        return menuList.stream().map(MenuResponseDTO::from).toList();
    }

    // JPQL 서브쿼리로 자신이 속한 카테고리의 평균 가격보다 비싼 메뉴 조회
    @Transactional(readOnly = true)
    public List<MenuResponseDTO> findMenusAboveCategoryAverage() {
        List<Menu> menuList = menuRepository
                .findMenusAboveCategoryAverage();

        return menuList.stream().map(MenuResponseDTO::from).toList();
    } // 리스트에 담기는건 조건을 통과한 메뉴(평균) / 평균은 기준일 뿐, 결과에는 안나옴

    // 카테고리별 메뉴 가격 일괄 인상
    @Transactional
    public PriceUpdateResponseDTO increasePriceByCategory(Integer categoryCode, Integer increaseRate) {
        Category category = findCategory(categoryCode);
        int updatedCount = menuRepository.increasePriceByCategory(category, increaseRate);

        return new PriceUpdateResponseDTO(updatedCount);
    }

    // 카테고리별 메뉴 조회
    @Transactional(readOnly = true)
    public List<MenuResponseDTO> findByCategoryNative(Integer categoryCode) {
        List<Menu> menuList = menuRepository
                .findByCategoryNative(categoryCode);

        return menuList.stream().map(MenuResponseDTO::from).toList();
    }
}
