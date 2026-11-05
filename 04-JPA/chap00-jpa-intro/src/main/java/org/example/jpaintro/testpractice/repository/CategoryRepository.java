package org.example.jpaintro.testpractice.repository;

import org.example.jpaintro.testpractice.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
 * [연습문제] Category 엔티티용 Repository
 *
 * 참고 코드 : org.example.jpaintro.repository.MenuRepository
 */
// [문제 2-1] (개념) 아래 상속 선언은 미리 작성되어 있습니다. (없으면 Service 주입이 실패해 앱 전체가 안 뜸)
//            제네릭 <Category, Integer> 에서 각각 무엇을 의미하는지 README 에 답해 보세요.
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    // [문제 2-2] 쿼리 메서드 : 카테고리 이름에 keyword 가 "포함된" 카테고리 목록 조회
    //            → SQL : WHERE category_name LIKE %keyword%

    // [문제 2-3] 쿼리 메서드 : 상위 카테고리 번호(refCategoryCode)로 하위 카테고리 목록 조회
    //            → SQL : WHERE ref_category_code = ?

    // [문제 2-4] 쿼리 메서드 : 상위 카테고리가 없는(최상위) 카테고리 목록 조회
    //            → SQL : WHERE ref_category_code IS NULL
}
