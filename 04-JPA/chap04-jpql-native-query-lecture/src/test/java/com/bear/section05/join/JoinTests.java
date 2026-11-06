package com.bear.section05.join;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JoinTests {

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
    * 1. 일반조인: 일반적인 SQL 조인을 의미 (내부 조인, 외부 조인, 컬렉션 조인, 세타 조인)
    * 2. 페치조인: JPQL에서 성능 최적화를 위해 제공하는 기능 (연관된 엔티티나 컬렉션을 한번에 조회 할수 있어 =>  즉시 로딩이 가능하다.)   (수정 전: 패치조인)
    * */

    // 일반 조인으로 실행을 시켜보자
    @Test
    public void 내부조인을_이용한_조회_테스트() {


        // 모든 메뉴를 가져오는데 즉시로딩으로 카테고리가 필요함,
        // 그리하여 사용하는 카테고리를 전부 select해서 가져옴
        // when
        String jpql = "SELECT m FROM menu_section05 m JOIN m.category c";
        /*
        * JOIN을 작성했지만 Select 대상은 menu(m)만 있어 조인을 해도 category를 함께 가져오지는 않는다.   (수정 전: mwne)
        *
        * 2. Menu -> @ManyToOne -> 즉시로딩(Eager)   (수정 전: Mene -> MenuToone)
        * 3. 메뉴 목록 조회 1번 + 카테고리 조회 N번 발생한다. 이것이 N+1 문제이다.   (수정 전: 발생 함 이것이 N+1번의 쿼리의 나감이 문제임.)
        * */
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).getResultList();

        // then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }

    @Test
    public void 페치조인을_이용한_조회_테스트() {

        /*[중요]
        * 페치조인??   (수정 전: 패치조인)
        * 처음 SQL을 실행할 때 연관된 엔티티까지 한 번에 조회해 온다. => 나중에 추가 쿼리를 날리지 않는다.   (수정 전: 처음 SQL실행 후 로딩할때의 조인 결과를 다 조회한 뒤에 사용한다. => 실행한 뒤에 필요하면 더 가지고 오는 방식임.)
        *
        * FETCH를 붙이면 조인한 Category까지 select에 포함되어 한 번의 쿼리로 함께 영속화된다.   (수정 전: FETCH을, 포함 한번의 쿼리로)
        *
        * */

        //when
        String jpql = "SELECT m FROM menu_section05 m JOIN FETCH m.category c";
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).getResultList();

        //then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);

    }

    @Test
    public void 외부조인을_이용한_조회_테스트() {

        // when
        String jpql = "SELECT m.menuName, c.categoryName " +
                      "FROM menu_section05 m RIGHT JOIN m.category c " +
                      "ORDER BY m.category.categoryCode";

        List<Object[]> menuList = entityManager.createQuery(jpql, Object[].class).getResultList();

        // then
        assertNotNull(menuList);

        menuList.forEach(row -> {
            Stream.of(row).forEach(col -> System.out.print(col + " "));
            System.out.println();
        });
    }

    @Test
    public void 컬렉션_조인을_이용한_조회_테스트() {

        // when
        String jpql = "SELECT c.categoryName, m.menuName FROM category_section05 c LEFT JOIN c.menuList m";
        List<Object[]> categoryList = entityManager.createQuery(jpql, Object[].class).getResultList();

        // then
        assertNotNull(categoryList);

        categoryList.forEach(row -> {
            Stream.of(row).forEach(col -> System.out.print(col + " "));
            System.out.println();
        });
    }



    /*
    * 크로스조인과 같다 => 조인이 되는 모든 경우의 수를 반환한다.
    * 카테고리와 메뉴의 조인 결과를 모두 조회한다.
    * */
    @Test
    public void 세타조인을_이용한_조회_테스트() {

        // when
        String jpql = "SELECT c.categoryName, m.menuName FROM category_section05 c, menu_section05 m";
        List<Object[]> categoryList = entityManager.createQuery(jpql, Object[].class).getResultList();

        // then
        assertNotNull(categoryList);

        categoryList.forEach(row -> {
            Stream.of(row).forEach(col -> System.out.print(col + " "));
            System.out.println();
        });

    }
}
