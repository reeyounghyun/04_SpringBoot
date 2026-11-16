package org.example.springdatajpa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.springdatajpa.entity.Category;
import org.example.springdatajpa.entity.Menu;

/*
* Projection -> 읽기전용객체
*
* - JPA Entity로만 응답받게 의존해야하는데
* - Entity에 없는 필드도 받을 수 있게
* - Projection을 만들어서 사용 할 수 있다.
* */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuResponseDTO {

    private Integer menuCode;
    private String menuName;
    private Integer menuPrice;
    private Integer categoryCode;
    private String categoryName;
    private String orderableStatus;

    public static MenuResponseDTO from(Menu menu) {
        Category category = menu.getCategory();

        return new MenuResponseDTO(
                menu.getMenuCode(),
                menu.getMenuName(),
                menu.getMenuPrice(),
                category.getCategoryCode(),
                category.getCategoryName(),
                menu.getOrderableStatus()
        );
    }
}
