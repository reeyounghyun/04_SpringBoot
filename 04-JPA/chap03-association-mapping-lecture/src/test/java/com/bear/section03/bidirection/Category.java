package com.bear.section03.bidirection;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity(name = "bidirection_category")
@Table(name = "tbl_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Category {

    @Id
    @Column(name = "category_code")
    private int categoryCode;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "ref_category_code")
    private Integer refCategoryCode;

    /*
    * 양방향 매핑에서는 주인이 아닌쪽에서는 속성을 지정해야함!
    * -> 주인이 아닌객체에 mappedBy를 써서 주인 객체의 필드명을 매핑시키면 양방향 적용!
    * */
    @OneToMany(mappedBy = "category")
    @ToString.Exclude // ToString에서 제외하겠다 라는 lombok 어노테이션
    private List<Menu> menuList;
}
