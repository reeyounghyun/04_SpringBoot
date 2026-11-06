package com.bear.section02.parameter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ParameterBindingTests {

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    @BeforeEach
    public void initManager() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterAll
    public static void closeFactory() {
        entityManagerFactory.close();
    }

    @AfterEach
    public void closeManager() {
        entityManager.close();
    }

    /*
    * 파라미터 바인딩
    * 1. 이름 기준 바인딩
    *  ':' 다음에 이름 기준 파라미터를 정한다.
    * 2. 위치 기준 바인딩
    * - '?' 다음에 값을 주고 위치값은 1부터 시작
    * */
    @Test
    public void 이름_기준_파라미터_바인딩_메뉴_목록_조회_테스트() {

        // given
        // 바인딩할 값: 문자열 "한우딸기국밥"을 menuNameParameter로 담는다.
        String menuNameParameter = "한우딸기국밥";

        // when
        // JPQL 문자열 작성: :menuName은 이름 기준 파라미터 (값은 setParameter로 넣음)   (수정 전: 문자을 jqpl에)
        String jpql = "SELECT m" + // 별칭 m인 Menu 엔티티 전체를 조회
                " FROM menu_section02 m" + // 엔티티명 menu_section02를 대상으로 , 별칭은 m
                " WHERE m.menuName = :menuName"; // menuNemw -> 이름 기준 파라미터 (값은 나중에 setParameter로 넣음)

        /*
        TypedQuery<Menu> query = entityManager.createQuery(jpql, Menu.class);
        TypedQuery<Menu> query2 = query.setParameter("menuName", menuNameParameter);
        List<Menu> menuList = query2.getResultList();
        */

        // 여러 건이 나올 수 있으므로  getResultList()로 List<Menu>를 받는다.   (수정 전: List<menu>)
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class)

                // 이름 기준 바인딩: JPQL의 menuName 자리에 menuNameParameter 값(한우딸기국밥)을 넣는다.
                .setParameter("menuName", menuNameParameter)
                .getResultList(); // 리스트 형식으로

        // then
        // assertNotNull: menuList가 null이 아닌지 확인   (수정 전: nulll)
        assertNotNull(menuList);

        menuList.forEach(System.out::println);

    }

    @Test
    public void 위치_기준_파라미터_바인딩_메뉴_목록_조회_테스트() {

        // given
        String menuNameParameter = "한우딸기국밥";

        // when
        String jpql = "SELECT m FROM menu_section02 m WHERE m.menuName = ?1";

        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class)
                .setParameter(1, menuNameParameter)
                .getResultList();

        // then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }
}
