package com.bear.section05.join;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.*;

@Entity(name="menu_section05")
@Table(name="tbl_menu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Menu {

    @Id
    @Column(name="menu_code")
    private int menuCode;

    @Column(name="menu_name")
    private String menuName;

    @Column(name="menu_price")
    private int menuPrice;

    // 여기  Category에서도 사용하여 양방향이라는 걸 알수 있다.
    @ManyToOne
    @JoinColumn(name="category_code")
    private Category category;

    @Column(name="orderable_status")
    private String orderableStatus;
}
