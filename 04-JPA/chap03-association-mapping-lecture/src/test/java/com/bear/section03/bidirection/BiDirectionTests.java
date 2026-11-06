package com.bear.section03.bidirection;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BiDirectionTests {

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

    @Test
    public void 양방향_연관관계_매핑_조회_테스트() {

        // given
        int menuCode = 10;
        int categoryCode = 10;

        Menu foundMenu = entityManager.find(Menu.class, menuCode); // fetch : eager
        Category foundCategory = entityManager.find(Category.class, categoryCode); // fetch : lazy

        // then
        /*
        * 양쪽 엔티티으, toString()이 서로 연관객체를 계속 출력해 재귀호출로 StackOverflowError가 발생한다.
        * 둘중 하나에 @ToString.Exclude를 사용하여 연관필드를 제거 해야한다..
        * */
        assertEquals(menuCode, foundMenu.getMenuCode());
        assertEquals(categoryCode, foundCategory.getCategoryCode());

        System.out.println(foundMenu);
        System.out.println(foundCategory);

        System.out.println("접근 전");
        foundCategory.getMenuList().forEach(System.out::println);
        System.out.println("접근 후");
    }


    @Test
    public void 양방향_연관관계_주인_객체를_이용한_삽입_테스트() {

        // given
        // 새메뉴 만들기 start
        Menu menu = new Menu();

        menu.setMenuCode(125);
        menu.setMenuName("연관관계주인메뉴");
        menu.setMenuPrice(10000);
        menu.setOrderableStatus("Y");

        //entityManager 기존에 있던거 찾아옴
        menu.setCategory(entityManager.find(Category.class, 4));
        // 새메뉴 만들기 end

        // when
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        entityManager.persist(menu);

        /*
        * 주인 쪽에 값을 넣어서 category_code가 채워진 Insert 한번으로 끝남
        * */
        entityTransaction.commit();

        // then
        Menu foundMenu = entityManager.find(Menu.class, menu.getMenuCode());
        assertEquals(menu.getMenuCode(), foundMenu.getMenuCode());
        System.out.println(foundMenu);
    }

    @Test
    public void 양방향_연관관계_주인이_아닌_객체를_이용한_삽입_테스트() {

        // given
        Category category = new Category();
        category.setCategoryCode(1004);
        category.setCategoryName("양방향카테고리");
        category.setRefCategoryCode(null);

        // when
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        /*
        * 메뉴와 연관관계가 없어 정상적으로 저장
        * -> mappedBy : 외래키를 관리하는 위치가 아닌곳(주인이 아닌곳)에 작성하자
        * -> FK를 관리하지 않아 카테고리를 추가해도 Menu자체에는 변하지 않음.
        * */
        entityManager.persist(category);

        entityTransaction.commit();

        // then
        Category foundCategory = entityManager.find(Category.class, category.getCategoryCode());
        assertEquals(category.getCategoryCode(), foundCategory.getCategoryCode());
        System.out.println(foundCategory);

    }

}
