package org.example.springdatajpa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.springdatajpa.entity.Category;
import org.example.springdatajpa.entity.Menu;

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
