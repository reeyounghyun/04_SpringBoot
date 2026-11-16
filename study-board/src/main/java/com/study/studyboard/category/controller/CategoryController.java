package com.study.studyboard.category.controller;


import com.study.studyboard.category.dto.CategoryResponseDTO;
import com.study.studyboard.category.service.CategoryService;
import com.study.studyboard.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
// 카테고리 기능만 모아둔 패키지
public class CategoryController {

    private final CategoryService categoryService;

    // 분류목록
    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<CategoryResponseDTO>>> categories() {

        List<CategoryResponseDTO> categories = categoryService.findCategories();
        return ResponseEntity
                .ok(ApiResponse.of(HttpStatus.OK, "분류 목록 조회 완료", categories));
    }



}
