package com.bear.section03.projection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.*;

import java.util.List;

@Entity(name="bidirection_category")
@Table(name="tbl_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BiDirectionCategory {

    @Id
    @Column(name="category_code")
    private int categoryCode;

    @Column(name="category_name")
    private String categoryName;

    @Column(name="ref_category_code")
    private Integer refCategoryCode;

    @OneToMany(mappedBy = "category")
    @ToString.Exclude
    private List<BiDirectionMenu> menuList;
}
