package org.example.jpaintro.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
 * [DB 컬럼과 자바 필드의 이름 규칙]
 *
 * | 위치               | 규칙                                       | 예시                                 |
 * |--------------------|--------------------------------------------|--------------------------------------|
 * | DB 컬럼 (tbl_menu) | snake_case : 소문자, 단어를 _ 로 연결      | menu_code, menu_name, category_code  |
 * | 자바 필드 (Menu)   | camelCase  : 두 번째 단어부터 첫 글자 대문자 | menuCode, menuName, categoryCode     |
 *
 * - @Column(name = "menu_code") 로 자바 필드와 DB 컬럼을 연결한다.
 * - Spring Boot 는 기본적으로 camelCase → snake_case 로 자동 변환해준다. (menuCode → menu_code)
 */
@Entity
@Table(name = "tbl_menu")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // PROTECTED 접근제어자

public class Menu {

    @Id // 식별자
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment로 만들어주겠다 DB를 참고함

    @Column(name = "menu_code")
    private Integer menuCode;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "menu_price")
    private Integer menuPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_code")
    private Category category;

    @Column(name = "orderable_status")
    private String orderableStatus;

    /*
     * [메뉴 등록용 생성자]
     * - 새 메뉴를 만들 때 사용한다. (Service 에서 MenuRequestDTO 값을 꺼내 new Menu(...) 로 생성)
     * - menuCode 는 받지 않는다. → DB 의 AUTO_INCREMENT 가 자동으로 번호를 매기기 때문 (@GeneratedValue)
     * - 값 세팅은 아래 change() 를 재사용해서, 등록/수정이 같은 로직을 쓰도록 했다.
     * - @NoArgsConstructor(PROTECTED) 로 기본 생성자는 막아두고, 이 생성자로만 메뉴를 만들게 한다.
     */
    public Menu(String menuName, Integer menuPrice, Category category, String orderableStatus) {
        change(menuName, menuPrice, category, orderableStatus);
    }

    public void change(String menuName, Integer menuPrice, Category category, String orderableStatus) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.category = category;
        this.orderableStatus = orderableStatus;
    }
}
