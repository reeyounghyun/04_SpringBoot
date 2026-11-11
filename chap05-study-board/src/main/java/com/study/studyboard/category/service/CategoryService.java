package com.study.studyboard.category.service;

import com.study.studyboard.category.dto.CategoryResponseDTO;
import com.study.studyboard.category.repository.CategoryRepository;
import com.study.studyboard.member.repository.MemberRepository;
import com.study.studyboard.post.repository.StudyPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // 분류목록
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(CategoryResponseDTO::from)
                .toList();
    }


}
