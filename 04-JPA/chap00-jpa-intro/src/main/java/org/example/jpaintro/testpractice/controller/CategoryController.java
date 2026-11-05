package org.example.jpaintro.testpractice.controller;

import lombok.RequiredArgsConstructor;
import org.example.jpaintro.testpractice.dto.CategoryRequestDTO;
import org.example.jpaintro.testpractice.dto.CategoryResponseDTO;
import org.example.jpaintro.testpractice.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * [연습문제] 카테고리 API
 *
 * 참고 코드 : org.example.jpaintro.controller.MenuController
 *
 * [문제 10] 각 메서드에 알맞은 매핑 어노테이션(@GetMapping 등)과
 *           매개변수 어노테이션(@PathVariable, @RequestBody, @RequestParam)을 붙이세요.
 *           URL 은 README 의 API 표를 보세요.
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // GET /api/categories
    public List<CategoryResponseDTO> findAll() {
        return categoryService.findAll();
    }

    // GET /api/categories/{categoryCode}
    public CategoryResponseDTO findById(Integer categoryCode) {
        return categoryService.findById(categoryCode);
    }

    // POST /api/categories
    public CategoryResponseDTO create(CategoryRequestDTO request) {
        return categoryService.create(request);
    }

    // PUT /api/categories/{categoryCode}
    public CategoryResponseDTO update(Integer categoryCode, CategoryRequestDTO request) {
        return categoryService.update(categoryCode, request);
    }

    // DELETE /api/categories/{categoryCode}
    public void delete(Integer categoryCode) {
        categoryService.delete(categoryCode);
    }

    // GET /api/categories/search?keyword=식사
    public List<CategoryResponseDTO> search(String keyword) {
        return categoryService.searchByName(keyword);
    }

    // GET /api/categories/{refCategoryCode}/children
    public List<CategoryResponseDTO> findChildren(Integer refCategoryCode) {
        return categoryService.findChildren(refCategoryCode);
    }

    // GET /api/categories/roots
    public List<CategoryResponseDTO> findRoots() {
        return categoryService.findRoots();
    }
}
