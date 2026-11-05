package org.example.jpaintro.testpractice.service;

import lombok.RequiredArgsConstructor;
import org.example.jpaintro.testpractice.dto.CategoryRequestDTO;
import org.example.jpaintro.testpractice.dto.CategoryResponseDTO;
import org.example.jpaintro.testpractice.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * [연습문제] 카테고리 비즈니스 로직
 *
 * 참고 코드 : org.example.jpaintro.service.MenuService
 * - 각 메서드의 throw 문을 지우고 직접 구현하세요.
 * - 필요한 곳에 @Transactional 을 붙이세요. (조회는 readOnly = true 도 고민해 보기)
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // [문제 4] 전체 조회 : findAll() → stream().map(...) 으로 DTO 리스트 변환
    public List<CategoryResponseDTO> findAll() {
        throw new UnsupportedOperationException("[문제 4] 를 풀어주세요.");
    }

    // [문제 5] 단건 조회 : 없는 번호면 404 + "카테고리를 찾을 수 없습니다."
    public CategoryResponseDTO findById(Integer categoryCode) {
        throw new UnsupportedOperationException("[문제 5] 를 풀어주세요.");
    }

    // [문제 6] 등록 : RequestDTO → new Category(...) → save() → DTO 반환
    public CategoryResponseDTO create(CategoryRequestDTO request) {
        throw new UnsupportedOperationException("[문제 6] 을 풀어주세요.");
    }

    // [문제 7] 수정 : findById 로 찾고(없으면 404) change() 로 값만 바꾼다. save() 는 호출하지 않는다!
    public CategoryResponseDTO update(Integer categoryCode, CategoryRequestDTO request) {
        throw new UnsupportedOperationException("[문제 7] 을 풀어주세요.");
    }

    // [문제 8] 삭제 : 없는 번호면 404, 있으면 삭제
    public void delete(Integer categoryCode) {
        throw new UnsupportedOperationException("[문제 8] 을 풀어주세요.");
    }

    // [문제 9-1] 이름 검색 : [문제 2-2] 쿼리 메서드 사용
    public List<CategoryResponseDTO> searchByName(String keyword) {
        throw new UnsupportedOperationException("[문제 9-1] 을 풀어주세요.");
    }

    // [문제 9-2] 하위 카테고리 조회 : [문제 2-3] 쿼리 메서드 사용
    public List<CategoryResponseDTO> findChildren(Integer refCategoryCode) {
        throw new UnsupportedOperationException("[문제 9-2] 를 풀어주세요.");
    }

    // [문제 9-3] 최상위 카테고리 조회 : [문제 2-4] 쿼리 메서드 사용
    public List<CategoryResponseDTO> findRoots() {
        throw new UnsupportedOperationException("[문제 9-3] 을 풀어주세요.");
    }
}
