package com.study.studyboard.category.dto;

import com.study.studyboard.category.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryResponseDTO {

    private Long id;
    private String name;

    public static CategoryResponseDTO from(Category Category) {
        return new CategoryResponseDTO(
                Category.getId(),
                Category.getName()
        );
    }
}


