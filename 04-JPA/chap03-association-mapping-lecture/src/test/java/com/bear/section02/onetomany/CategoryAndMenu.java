package com.bear.section02.onetomany;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "category_and_menu")
@Table(name = "tbl_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryAndMenu {

    @Id
    @Column(name = "category_code")
    private int categoryCode;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "ref_category_code")
    private Integer refCategoryCode;

    /*
    * 연관관계별 기본 fetch 전략
    * - @ManyToOne : 엔티티 1개를 가져옴 (메뉴 → 카테고리 하나)
    *   → 기본 fetch = EAGER (1개라 같이 가져와도 부담이 적음)
    * - @OneToMany : 엔티티 여러 개를 가져옴 (카테고리 → 메뉴 리스트)
    *   → 기본 fetch = LAZY (개수를 알 수 없어 실제 사용할 때 가져옴)
    * */
    @JoinColumn(name = "category_code")
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Menu> menuList;

}
