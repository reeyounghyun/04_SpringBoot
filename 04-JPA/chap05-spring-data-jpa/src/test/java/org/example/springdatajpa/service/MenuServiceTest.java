package org.example.springdatajpa.service;

import org.example.springdatajpa.dto.MenuResponseDTO;
import org.example.springdatajpa.entity.Category;
import org.example.springdatajpa.entity.Menu;
import org.example.springdatajpa.repository.CategoryRepository;
import org.example.springdatajpa.repository.MenuRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/*
 * MenuService의 비즈니스 로직을 검증하는 단위테스트
 * Mockito로 만든 가짜 Repository를 사용할것임
 *
 * 실제 Repository를 사용하지않고 테스트에 필요한 동작만 설정 할 수 있음
 *
 * */
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    // @Mock가 가짜 레파지토리를 가지고 올거야.
    // @Mock가 가짜 객체를 생성한다.
    @Mock
    private MenuRepository menuRepository;

    // 의존성: MenuService가 카테고리 조회에 사용하는 Repository (가짜 객체로 대체)
    @Mock
    private CategoryRepository categoryRepository;


    // 테스트 대상 : 데스트 대상인 menuService 객체를 생성한다.
    // Mock으로 만든 가짜객체를 생성자로 주입해준다.
    @InjectMocks
    private MenuService menuService;

    @Test
    void 최저가격이_null이면_예외가_발생한다() {
        // 예외를 잘 던지는가?를 확인해야한다.
        // 최저가 이면 예외가 발생하낟.

        // given
        Integer minPrice = null;
        Integer maxPrice = 10000;
        String status = "Y";

        // 언제 호출하는지 궁금?
        // assertThrows 예외 던짐
        // when
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> menuService.searchByPriceRange(minPrice, maxPrice, status)
        );

        // 원하는 상태코드가 잘 갔는지?
        // then
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

        // Mock 매서드가 (menuRepository)가호출되었는지 확인 할거임.
        /*
        * never()는 호출 횟수가 0번이여야 한다.
        * any() 매개변수 값과 관계없이 모든 값을 허요
        *
        * */
        verify(menuRepository, never()).searchByPriceRange(any(), any(), any());
    }

    @Test
    void 주문가능상태가_잘못되면_예외가_발생한다() {
        // given
        Integer minPrice = 3000;
        Integer maxPrice = 5000;
        String status = "A";

        // when
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> menuService.searchByPriceRange(minPrice, maxPrice, status)
        );

        // then
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(menuRepository, never()).searchByPriceRange(any(), any(), any());
    }

    // 정상요청이면 조회결과를 디티오로 반환한다?
    @Test
    void 주문가능상태가_Y이면_메뉴를_DTO로_반환한다() {
        // given
        Integer minPrice = 3000;
        Integer maxPrice = 5000;
        String status = "Y";

        // 테스트에 필요한 가짜 Category
        Category category = mock(Category.class);

        // when
        // when().thenReturn() getter호출시 반환할 값을 미리 설정
        when(category.getCategoryCode()).thenReturn(4);
        when(category.getCategoryName()).thenReturn("한식");

        // 임시 메뉴
        // repository에서 메뉴를 조회했을때 반환할 값을 가정
        Menu menu = new Menu("테스트메뉴", 5000, category, status);

        //minPrice, maxPrice, status 호출해서
        // when().thenReturn() repository.searchByPriceRange 메서드 호출시 반환할 값을 미리 설정
        when(menuRepository.searchByPriceRange(minPrice, maxPrice, status))
                .thenReturn(List.of(menu));

        // when
        List<MenuResponseDTO> result = menuService.searchByPriceRange(minPrice, maxPrice, status);
        // 호출할 때

        // then
        // 결과가 잘 나왔는지 확인
        assertEquals("테스트메뉴", result.get(0).getMenuName());
        assertEquals(4, result.get(0).getCategoryCode());
        assertEquals("한식", result.get(0).getCategoryName());

        // searchByPriceRange 한 번 호출되었는지 확인
        verify(menuRepository).searchByPriceRange(minPrice, maxPrice, status);
    }
}