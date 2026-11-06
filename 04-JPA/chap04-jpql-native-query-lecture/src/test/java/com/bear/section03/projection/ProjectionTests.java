package com.bear.section03.projection;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProjectionTests {

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
* 프로젝션 : SELECT 절에 조회할 대상을 지정하는것
* ex) (SELECT {프로젝션대상} FROM)
*
* 프로젝션의 종류
* 1. 엔티티의 프로젝션
*  - 원하는 객체를 바로 조회할 수 있고 조회된 엔티티는 영속성 컨텍스트가 관리한다
* 2. 임베디드 타입 프로젝션
*  - 엔티티 안에 @Embedded로 넣은 값 객체(예: 주소)를 조회할 때 사용   (수정 전: 별도로 분리하지 않고, 엔티티를 조회 하고싶을 때 사용)
*  - 엔티티와 거의 동일하면서 대신 조회의 시작이 될 수 없다 (From에 못 쓰임)
* 3. 스칼라 타입 프로젝션
*  - 숫자, 문자, 날짜 같은 기본 데이터 타입 (영속성 컨텍스트에서 관리 안됨)
* 4.  new 명령어를 이용한 프로젝션
*  - 다양한 종류의 단순 값을 DTO 로 바로 조회하는 방식 이다. (영속성 컨텍스트에서 관리 안된다)
* */
    @Test
    public void 단일_엔티티_프로젝션_테스트() {

        // when         엔티티 menu_section03, 별칭 m
        String jpql = "SELECT m FROM menu_section03 m";
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).getResultList();  // 목록조회
        //  List<Menu> menuList => 영속 상태


        // then
        assertNotNull(menuList);  // null이 아니면 통과


        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();


        // 메뉴 이름을 test로 변경
        menuList.get(1).setMenuName("test");

        entityTransaction.commit();
        // 영속성 컨텍스트가 관리를 하고있어 변경된걸 확인 할 수 있다.

    }

    @Test
    public void 양방향_연관관계_엔티티_프로젝션_테스트() {

        // given
        int menuCodeParameter = 3;

        // when
        String jpql = "SELECT m.category FROM bidirection_menu m WHERE m.menuCode = :menuCode";
        BiDirectionCategory categoryOfMenu = entityManager.createQuery(jpql, BiDirectionCategory.class)
                .setParameter("menuCode", menuCodeParameter)
                .getSingleResult();

        // then
        assertNotNull(categoryOfMenu);
        System.out.println(categoryOfMenu);

        assertNotNull(categoryOfMenu.getMenuList());
        categoryOfMenu.getMenuList().forEach(System.out::println);

    }

    @Test
    public void TypedQuery를_이용한_스칼라_타입_프로젝션_테스트() {
        // 영속x

        // when       카테고리 1개 카테고리 네임 1개씩 꺼내, 엔티티가 아님
        String jpql = "SELECT c.categoryName FROM category_section03 c";
        List<String> categoryNameList = entityManager.createQuery(jpql, String.class).getResultList();

        // then
        assertNotNull(categoryNameList);
        categoryNameList.forEach(System.out::println);

    }

    @Test
    public void Query를_이용한_스칼라_타입_프로젝션_테스트() { // 영속 x

        /*
        * 조회하려는 컬럼이 1개 일경우에는 TypedQuery로 반환타입을 명시 할 수 있지만,
        * 2개 이상일 경우에는 명시하지 못하므로 Object[] 형태로 데이터를 받아서 형변환 해서 사용한다.
        * */

        // when
        // 조회 대상이 2개(categoryCode, categoryName)라 한 행이 Object[]로 반환된다.   (수정 전: 오브젝트 쿼리로 가져와진다.)
        String jpql = "SELECT c.categoryCode, c.categoryName FROM category_section03 c";

        // 결과 타입을 지정하지 않으면 Query 객체가 생성된다.   (수정 전: 오브젝트 쿼리를 사용해 진다.)
        List<Object[]> categoryList = entityManager.createQuery(jpql).getResultList();


        // then
        assertNotNull(categoryList);
        categoryList.forEach(row -> {
            Arrays.stream(row).forEach(System.out::println);
        });

    }

    @Test
    public void new_명령어를_활용한_프로젝션_테스트() {
        // 영속 X

        // .CategoryInfo 필드는 타입을 가져와서 =>  객체 형태로 불러오기 위해
        // 스칼라: categoryCode categoryName

        /*
        * SELECT 조회하겠다
        * new 조회 결과로 객체를 새로 만들겠다
        *  com.bear.section03.projection.CategoryInfo 만들 클래스는 CategoryInfo, 패키지까지 포함한 전체 경로를 작성   (수정 전: 경로르)
        * c.categoryCode, c.categoryName 생성자에 넘길 값, categoryCode 필드와 categoryName 필드를 넘김
        * FROM category_section03 c 엔티티 FROM category_section03 을 대상으로 , 별칭은 c
        * */
        // when
        String jpql = "SELECT new com.bear.section03.projection.CategoryInfo(c.categoryCode, c.categoryName) FROM category_section03 c";

        /*
        * jpql, CategoryInfo.class 위에서 만은 JPQL 문자열로 쿼리 객체를 만든다.
        * CategoryInfo.class 결과 타입은 CategoryInfo 라고 알려준다,   List<CategoryInfo>가 만들어짐
        * .getResultList() 쿼리를 실행해 결과 여러 건으 List로 받는다.
        * List<CategoryInfo> categoryInfoList =  그 결과를 categoryInfoList에 담는다.
        * */
        List<CategoryInfo> categoryInfoList = entityManager.createQuery(jpql, CategoryInfo.class).getResultList();

        // then
        // assertNotNull: categoryInfoList가 null이 아닌지 확인   (수정 전: NULL인지 확인)
        assertNotNull(categoryInfoList);
        categoryInfoList.forEach(System.out::println);
    }
}
