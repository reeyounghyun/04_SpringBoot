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

/**
 * [Service 계층] 비즈니스 로직 + 트랜잭션 경계
 *
 * - 상속받은 기본 CRUD 메서드(save, findById ...)에는 기본 트랜잭션이 적용되어 있지만,
 *   여러 Repository 작업을 "하나의 단위"로 묶으려면 Service 에 @Transactional 을 붙인다.
 * - 조회 전용 메서드는 @Transactional(readOnly = true) 로 변경 감지 등의 불필요한 작업을 줄인다.
 */
@Service // 빈을 등록하자
@RequiredArgsConstructor // final 필드(menuRepository)를 받는 생성자 자동 생성
@Transactional(readOnly = true) // 기본은 읽기 전용, 변경 메서드에만 @Transactional 을 따로 붙인다
public class MenuService {

    private final MenuRepository menuRepository;

    // 전체 조회 : findAll()
    public List<MenuResponseDTO> findAll() {

        // 데이터베이스에서 조회가됨!
        List<Menu> menuList = menuRepository.findAll();

        // Entity → DTO 변환
        List<MenuResponseDTO> list = menuList.stream()
                .map(menu -> MenuResponseDTO.from(menu))
                .toList();

        return list;
    }

    // 단건 조회 : findById() → Optional<Menu> 반환
    public MenuResponseDTO findById(Integer menuCode) {

        /*
         * [Optional 처리 방법]
         * - orElse(기본값)     : 없으면 지정한 값 사용
         * - orElseGet(공급자)  : 없으면 대체값 생성
         * - orElseThrow()      : 없으면 예외 발생   ← 여기서는 404 응답
         * - ifPresent(처리)    : 있을 때만 처리
         */
        Menu menu = findMenu(menuCode);

        return MenuResponseDTO.from(menu);
    }

    // 이름 부분 검색 : 쿼리 메서드 findByMenuNameContaining()
    public List<MenuResponseDTO> searchByName(String keyword) {
        return menuRepository.findByMenuNameContaining(keyword).stream()
                .map(MenuResponseDTO::from)
                .toList();
    }

    // 전체 개수 : count()
    public long count() {
        return menuRepository.count();
    }

    // 특정 가격보다 비싼 메뉴 존재 여부 : 쿼리 메서드 existsByMenuPriceGreaterThan()
    public boolean existsPriceGreaterThan(int menuPrice) {
        return menuRepository.existsByMenuPriceGreaterThan(menuPrice);
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

        /*
         * [save() 동작]
         * - ID 유무로 INSERT/UPDATE 를 고르는 게 아니라 "새 엔티티인지" 판정한다.
         *   새 엔티티 → EntityManager.persist() / 아니면 → EntityManager.merge()
         * - merge 는 전달한 객체와 다른 인스턴스를 돌려줄 수 있으므로 "반환값"을 사용한다.
         * - save() 호출 = 즉시 SQL 실행/커밋 이 아니다. (IDENTITY 전략은 PK 를 얻기 위해 바로 INSERT 됨)
         */
        Menu savedMenu = menuRepository.save(menu);

        return MenuResponseDTO.from(savedMenu);
    }

    // update 메서드
    @Transactional
    public MenuResponseDTO update(Integer menuCode, MenuRequestDTO request) {

        // id로 수정할 메뉴 정보 찾아오기
        Menu menu = findMenu(menuCode);

        // 값 변경 → save() 를 호출하지 않아도 트랜잭션 종료 시 변경 감지(Dirty Checking)로 UPDATE
        menu.change(
                request.getMenuName(),
                request.getMenuPrice(),
                request.getCategoryCode(),
                request.getOrderableStatus());

        return MenuResponseDTO.from(menu);
    }

    // 번호로 삭제 : existsById() + deleteById()
    @Transactional
    public void delete(Integer menuCode) {

        // deleteById() 는 대상이 없으면 아무것도 하지 않으므로, 없는 번호는 먼저 확인해서 404 를 준다
        if (!menuRepository.existsById(menuCode)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "메뉴를 찾을 수 없습니다.");
        }

        menuRepository.deleteById(menuCode);
    }

    // 이름으로 삭제 : 쿼리 메서드 deleteByMenuName() → 반드시 트랜잭션 안에서 호출
    @Transactional
    public void deleteByName(String menuName) {
        menuRepository.deleteByMenuName(menuName);
    }

    // 공통 : 번호로 엔티티 조회, 없으면 404
    private Menu findMenu(Integer menuCode) {
        return menuRepository.findById(menuCode)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "메뉴를 찾을 수 없습니다."));
    }
}
