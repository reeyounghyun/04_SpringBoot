package org.example.exercise.practice.service;

import lombok.RequiredArgsConstructor;
import org.example.exercise.practice.repository.ExerciseMenuRepository;
import org.example.springdatajpa.repository.CategoryRepository;
import org.springframework.stereotype.Service;

/*
 * ===============================================================
 *  [연습문제] Service 작성
 *  - Repository 메서드를 호출하고, 조회 결과는 MenuResponseDTO로 변환해서 반환한다.
 *  - 조회 메서드: @Transactional(readOnly = true)
 *  - 수정 메서드: @Transactional  (readOnly 없이!)
 * ===============================================================
 */
@Service
@RequiredArgsConstructor
public class ExerciseMenuService {
    private final ExerciseMenuRepository exerciseMenuRepository;
    private final CategoryRepository categoryRepository;

    /*
     * [3-1] 이름이 특정 단어로 시작하는 메뉴 조회
     * - 반환: List<MenuResponseDTO>
     */


    /*
     * [3-2] 이름에 키워드가 포함된 주문 가능 메뉴 조회
     * - 반환: List<MenuResponseDTO>
     */


    /*
     * [3-3] 전체 평균 가격보다 싼 메뉴 조회
     * - 반환: List<MenuResponseDTO>
     */


    /*
     * [3-4] 카테고리의 모든 메뉴를 주문 불가로 일괄 변경
     * - categoryCode로 Category를 먼저 조회한 뒤 Repository에 넘긴다.
     * - 카테고리가 없으면 예외를 던진다.
     * - 반환: 변경된 개수(int)
     */


    /*
     * [3-5] 가격이 minPrice 이상인 메뉴 조회 (네이티브 쿼리)
     * - 반환: List<MenuResponseDTO>
     */


    /*
     * [4-1] 카테고리별 메뉴 가격 일괄 인하
     * - categoryCode로 Category를 조회
     * - Repository의 discountPriceByCategory 호출
     * - 반환: DiscountResponseDTO (변경된 개수 담기)
     */

}
