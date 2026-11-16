package org.example.springdatajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_menu")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_code")
    private Integer menuCode;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "menu_price")
    private Integer menuPrice;

    // 다대일(N:1) 연관관계: 메뉴 여러 개가 카테고리 하나에 속한다.
    // fetch = LAZY(지연 로딩): Menu를 조회할 때 Category는 바로 가져오지 않고 프록시(가짜 객체)로 채워 두었다가,
    //                         category.getCategoryName()처럼 실제로 사용할 때 SELECT를 실행한다.
    //                         (기본값 EAGER는 Menu를 조회할 때마다 Category까지 즉시 JOIN/SELECT 한다)
    @ManyToOne(fetch = FetchType.LAZY)
    // tbl_menu의 FK 컬럼 category_code로 tbl_category와 조인한다. (연관관계의 주인 = Menu)
    @JoinColumn(name = "category_code")
    // 컬럼 값(Integer categoryCode) 대신 Category 엔티티 객체를 참조한다.
    private Category category;

    @Column(name = "orderable_status")
    private String orderableStatus;

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
