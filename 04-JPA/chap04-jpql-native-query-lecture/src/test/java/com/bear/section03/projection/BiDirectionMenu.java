package com.bear.section03.projection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.*;

@Entity(name="bidirection_menu")
@Table(name="tbl_menu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BiDirectionMenu {

    @Id
    @Column(name="menu_code")
    private int menuCode;
    @Column(name="menu_name")
    private String menuName;
    @Column(name="menu_price")
    private int menuPrice;

    @ManyToOne
    @JoinColumn(name="category_code")
    private BiDirectionCategory category;

    @Column(name="orderable_status")
    private String orderableStatus;
}
