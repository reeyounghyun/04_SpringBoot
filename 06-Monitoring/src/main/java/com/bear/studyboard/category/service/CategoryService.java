package com.bear.studyboard.category.service;

import com.bear.studyboard.category.dto.CategoryResponseDTO;
import com.bear.studyboard.category.entity.Category;
import com.bear.studyboard.category.repository.CategoryRepository;
import com.bear.studyboard.common.error.BusinessException;
import com.bear.studyboard.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findCategories() {

        List<Category> categories = categoryRepository.findAllByOrderByIdAsc();

        List<CategoryResponseDTO> categoryResponseDTOList = categories.stream()
                .map(category -> CategoryResponseDTO.from(category))
                .toList();

        return categoryResponseDTOList;
    }

    @Transactional(readOnly = true)
    public Category findRequiredCategory(long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        return category;
    }
}
