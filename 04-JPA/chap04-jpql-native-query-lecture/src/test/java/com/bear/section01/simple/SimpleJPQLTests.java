package com.bear.section01.simple;

import jakarta.persistence.*;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleJPQLTests {

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
    * JPQL
    * - SELECT, UPDATE DELETE의 사용은 SQL과 동일
    *  SQL과 달라지는 점
    * - INSERT => persist() 영속 메서드는 사용해야함
    * - 엔티티의 별칭을 필수 사용   (수정 전: 앤티티)
    *
    *
    * - 쿼리 객제: 쿼리를 실행하고 결과를 전달해주는 역할을 한다.
    * - TypedQuery : 반환할 타입을 명확하게 지정하는 방식
    * - Query: 반환할 티입을 지정할 수 없을 때 Object형으로 받는 방식
    *
    * getSingleList() : 결과가 정확히 한 행일 경우 사용 (많으면 예외 방생)
    * getResilList() : 결과가 두행이상일떄 사용 없으면 빈 컬렉션 반환
    *
    * */
    @Test
    public void TypedQuery를_이용한_단일메뉴_조회_테스트() {


        // SQL 컬럼 테이블명
        // JPQL 필드명 엔티티명
        /*
        * 별칭 m은 컬럼이 아니라 엔티티에 붙은 이름 이다.
        *
        * */
        // when
        String jpql = "SELECT m.menuName " + //  별칭 m인 Menu 엔티티의 menuName 필드 값을 조회하겠다.
                      "FROM menu_section01 as m " +   // 엔티티명 menu_section01을 대상으로, 별칭은 m (실제 테이블은 tbl_menu)   (수정 전: 앤티티명.menu_section01)
                      "WHERE m.menuCode = 7"; // 메뉴 코드 필드 값이  7인 메뉴만

        // menu_section01에서, menuCode가 7인 메뉴의 이름만 조회
        // 결과 타입을 String.class로 지정 -> TypedQuery<String> 생성
        TypedQuery<String> query = entityManager.createQuery(jpql, String.class);

        // 문자열인 resultMenuName이라는 변수에 쿼리를 실행하여 조회한 결과를 넣는다.

        // query로 getSingleResult()를 실행해 결과를 문자열 resultMenuName에 담는다.   (수정 전: resulMenuName에 담아 사용하고)
        String resultMenuName = query.getSingleResult();

        // 결과에 민트미역국이 조회가 되는지?
        assertEquals("민트미역국", resultMenuName);

    }

    @Test
    public void Query를_이용한_단일메뉴_조회_테스트() {

        // given
        String jpql = "SELECT m.menuName" +  //  별칭인 m인 Menu 엔티티의 menuName필드 값을 조회
                " FROM menu_section01 as m" + // 엔티티명 menu_section01을 대상으로, 별칭은 m   (수정 전: menu_serction01)
                " WHERE m.menuCode = 7"; // menuCode 필드 값이  7인 메뉴만 조회

        // when
        // 결과 타입을 지정하지 않으면 query 객체가 생성되고, 결과는 Object 타입으로 반환된다.
        Query query = entityManager.createQuery(jpql);
        Object resultMenuName = query.getSingleResult();

        System.out.println(String.valueOf(resultMenuName));

        // then
        // 반환된 Object 타입이 String 타입인지 확인?
        assertTrue(resultMenuName instanceof String);
        // 조회된 메뉴 이름이 민트미역국인지 확인
        assertEquals("민트미역국", resultMenuName);

    }

    @Test
    public void TypedQuery를_이용한_단일행_조회_테스트() {

        // when
        String jpql = "SELECT m" + // 별칭 m인 Menu 엔티티 전체(한 행)를 조회
                " FROM menu_section01 as m" + // 엔티티명 menu_section01을 대상으로, 별칭은 m   (수정 전: 앤티티명 menu_serction01)
                " WHERE m.menuCode = 7"; // menuCode 필드 값이 7인 메뉴만 조회

        // 결과 타입으로 Menu.clss로 지정 하고  TypedQuery<Menu> 생성
        TypedQuery<Menu> query = entityManager.createQuery(jpql, Menu.class);

        // 쿼리를 실행해 결과 1건(Menu 엔티티)을 foundMenu에 담는다.   (수정 전: 앤티티, fondMenu)
        Menu foundMenu = query.getSingleResult();

        // then
        // 조회한 메뉴코드가 7인지 확인
        assertEquals(7, foundMenu.getMenuCode());
        System.out.println(foundMenu);

    }

    @Test
    public void TypedQuery를_이용한_다중행_조회_테스트() {

        // when
        String jpql = "SELECT m FROM menu_section01 as m"; // WHERE 조건 없이 Menu 엔티티 전체(모든 행)을 조회
        TypedQuery<Menu> query = entityManager.createQuery(jpql, Menu.class);    // 반환 타입을 row와 매핑할 엔티티 타입으로 설정

        // 결과가 여러 건이면 getResultList()로  List<Menu>를 받는다 (결과가 없으면 빈 리스트 반환)
        List<Menu> foundMenuList = query.getResultList();

        // then
        // 결과 리스트가 null이 아닌지 확인 후 전체 출력
        assertNotNull(foundMenuList);
        foundMenuList.forEach(System.out::println);

    }

    @Test
    public void Query를_이용한_다중행_조회_테스트() {

        // when
        String jpql = "SELECT m FROM menu_section01 as m";
        Query query = entityManager.createQuery(jpql);

        List<Menu> foundMenuList = query.getResultList();

        // then
        assertNotNull(foundMenuList);
        foundMenuList.forEach(System.out::println);

    }

    @Test
    public void distinct를_활용한_중복제거_여러_행_조회_테스트() {

        // when
        String jpql = "SELECT DISTINCT m.categoryCode FROM menu_section01 m";
        TypedQuery<Integer> query = entityManager.createQuery(jpql, Integer.class);
        List<Integer> categoryCodeList = query.getResultList();

        // then
        assertNotNull(categoryCodeList);
        categoryCodeList.forEach(System.out::println);

    }

    @Test
    public void in_연산자를_활용한_조회_테스트() {

        // when
        String jpql = "SELECT m" + // 별칭 m인 Menu 엔티티 전체를 조회   (수정 전: 앤티티)
                " FROM menu_section01 m " +  // 엔티티명 menu_section01을 대상으로, 별칭은 m
                "WHERE m.categoryCode IN (6, 10)"; // categoryCode가 6 또는 10인 메뉴만 ( IN= 괄호 안 값 중 하나와 일치)

        // 여러 건이므로 getResultList()로 List<Menu>를 받는다.
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).getResultList();

        // then
        // assertNotNull(menuList): menuList가 null이 아닌지 확인   (수정 전: noNull)
        assertNotNull(menuList);

        menuList.forEach(System.out::println);
    }

    @Test
    public void like_연산자를_활용한_조회_테스트() {

        // when
        String jpql = "SELECT m " + // 별핑 m인 Menu 엔티티 전체를 조회
                "FROM menu_section01 m" + // 엔티티명 menu_section01을 대상으로, 별칭은 m   (수정 전: 앤티티명)
                " WHERE m.menuName LIKE '%마늘%'"; // menuName이 '마늘'이 포함된 메뉴만(% = 0개 이상의 아무 문자)

        // 여러 건이므로 getResultList()로 List<Menu>를 받는다.   (수정 전: List<menu>)
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).getResultList();

        // then
        // assertNotNull(menuList): menuList가 null이 아닌지 확인   (수정 전: NoNULL, menuList기)
        assertNotNull(menuList);

        // 조회된 메뉴 전체 출력
        menuList.forEach(System.out::println);
    }

}
