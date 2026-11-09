package org.example.exercise.practice;

/*
 * ===============================================================
 *  [연습문제] Part 1. 개념 확인 / Part 2. 틀린 곳 찾기
 *  - 코드 작성 문제(Part 3, 4)는 controller / service / repository / dto 패키지에 있다.
 *  - 답은 이 주석 아래 "내 답:" 에 직접 적어본다.
 * ===============================================================
 *
 * ---------------------------------------------------------------
 *  Part 1. 개념 확인
 * ---------------------------------------------------------------
 *
 * [1-1] 맞으면 O, 틀리면 X
 *  1) JPQL은 테이블 이름(tbl_menu)과 컬럼 이름(menu_price)을 사용한다.          내 답:
 *  2) @Modifying이 없으면 @Query의 update 쿼리는 실행되지 않는다.               내 답:
 *  3) Containing 키워드를 쓰면 Spring이 %를 자동으로 붙여준다.                 내 답:
 *  4) 서브쿼리 안의 평균값은 결과 리스트에 함께 담긴다.                         내 답:
 *  5) 벌크 수정 메서드에는 @Transactional(readOnly = true)를 붙인다.            내 답:
 *
 * [1-2] 다음 에러 메시지의 원인을 한 줄로 쓰시오.
 *  No EntityManager with actual transaction available for current thread
 *  - cannot reliably process 'flush' call
 *  내 답:
 *
 * [1-3] @Modifying에서 clearAutomatically = true가 없으면 어떤 문제가 생길 수 있는지 쓰시오.
 *  내 답:
 *
 * ---------------------------------------------------------------
 *  Part 2. 틀린 곳 찾기
 * ---------------------------------------------------------------
 *
 * [2-1] 실행하면 에러가 난다. 틀린 곳 2군데를 찾으시오.
 *
 *  @Query("""
 *          select menu
 *          from Menu menu
 *          where menu.menuName like concat('%', :word '%')
 *          """)
 *  List<Menu> searchMenu(@Param("keyword") String keyword);
 *
 *  내 답:
 *
 * [2-2] 서버가 뜨지 않는다. 원인을 찾으시오.
 *
 *  @Query("""
 *          select menu
 *          from Menu menu
 *          join fetch menu
 *          where menu.menuPrice >= 10000
 *          """)
 *  List<Menu> findExpensiveMenus();
 *
 *  내 답:
 *
 * [2-3] 네이티브 쿼리인데 실행하면 에러가 난다. 틀린 곳 3군데를 찾으시오.
 *
 *  @Query(value = """
 *          select menu.*
 *          from Menu menu
 *          where menu.categoryCode = :categoryCode
 *          """)
 *  List<Menu> findByCategoryNative(Integer categoryCode);
 *
 *  내 답:
 */
public class PracticeConceptQuiz {
}
