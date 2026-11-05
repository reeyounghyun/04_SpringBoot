package com.section03.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class A_EntityLifeCycleTest {

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpabook");
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
    public void 준영속성_detach_테스트() {

        Menu foundMenu1 = entityManager.find(Menu.class, 11); // 영속성 컨텍스트에 등록
        Menu foundMenu2 = entityManager.find(Menu.class, 12); // 영속성 컨텍스트에 등록

        // 준영속 상태 : 컨텍스트로 관리하던 엔티티 객체를 관리하지 않는 상태가 되면 준영속 상태라고 함.
        entityManager.detach(foundMenu2); // foundMenu2 준영속 상태로 변경

        foundMenu1.setMenuPrice(5000); // 영속상태에서의 변경 1만원이었는데 5천원으로 변경

        foundMenu2.setMenuPrice(5000); // 준영속상태에서의 변경

        assertEquals(5000, entityManager.find(Menu.class, 11).getMenuPrice()); // 영속성 1차 캐시에서 변경된 내용을 가지고 온다.

        assertEquals(5000, entityManager.find(Menu.class, 12).getMenuPrice());
    }

    @Test
    public void 준영속성_clear_테스트() {

        Menu foundMenu1 = entityManager.find(Menu.class, 11);
        Menu foundMenu2 = entityManager.find(Menu.class, 12);

        entityManager.clear(); // 둘 다 영속상태를 해제함, 둘 다 준영속으로 만든다.

        foundMenu1.setMenuPrice(5000);

        foundMenu2.setMenuPrice(5000);


        assertEquals(5000, entityManager.find(Menu.class, 11).getMenuPrice());

        assertEquals(5000, entityManager.find(Menu.class, 12).getMenuPrice());
    }


    @Test
    public void close_테스트() {

        // 1. 컨텍스트 등록 (find()로 조회한 엔티티는 영속 상태)
        Menu foundMenu1 = entityManager.find(Menu.class, 11);
        Menu foundMenu2 = entityManager.find(Menu.class, 12);


        // 2. 영속성 컨텍스트를 닫는다, 닫힌 엔티티 매니저는 재사용이 안됨
        // 3. 관리하던 엔티티가 모두 준영속이 된다 (평범한 자바 객체처럼 됨)
        entityManager.close();

        foundMenu1.setMenuPrice(5000);

        foundMenu2.setMenuPrice(5000);

        // 4. 닫힌 매니저로는 조회할 수 없으므로 새 엔티티 매니저를 만들어 다시 조회한다.
        //    (새 매니저는 @AfterEach 에서 닫힌다)
        entityManager = entityManagerFactory.createEntityManager();

        assertEquals(5000, entityManager.find(Menu.class, 11).getMenuPrice());

        assertEquals(5000, entityManager.find(Menu.class, 12).getMenuPrice());
    }

    /*
    * merge() :
    * 준영속 엔티티 객체의 식별자 값으로 1차 엔티티 객체를 조회,
    *   => 1차 캐시에서 엔티티 객체를 조회한다. (1차 캐시에 없으면 DB에서 조회)
    * 조회한 엔티티 객체에 준영속 상태의 엔티티 객체의 값을 병합해서 엔티티 객체를 반환
    *  => 영속성 컨텍스트에 관리를 받지 않는 엔티티 값을 영속성 컨텍스트에 관리되는 엔티티에 복사하는 기능
    * */
    @Test
    public void 병합_merge_수정_테스트() {

        Menu menuToDetach = entityManager.find(Menu.class, 3); // 영속상태
        entityManager.detach(menuToDetach); // 준영속

        menuToDetach.setMenuName("수박죽"); // 바꿔줬을 때 => 반영 안됨!

        Menu refoundMenu = entityManager.find(Menu.class, 3); // refoundMenu 영속상태

        //
        System.out.println(menuToDetach.hashCode());
        System.out.println(refoundMenu.hashCode());

        entityManager.merge(menuToDetach); // 준영속 객체의 값을 영속 객체로 옮기는 행위

        Menu mergedMenu = entityManager.find(Menu.class, 3);
        assertEquals("수박죽", mergedMenu.getMenuName());
    }

    @Test
    public void 병합_merge_수정1_테스트() {

        Menu menuToDetach = entityManager.find(Menu.class, 2);
        entityManager.detach(menuToDetach);

        menuToDetach.setMenuCode(999);
        menuToDetach.setMenuName("수박죽");

        entityManager.merge(menuToDetach);

        Menu mergedMenu = entityManager.find(Menu.class, 999);
        assertEquals("수박죽", mergedMenu.getMenuName());

    }

    @Test
    public void 병합_merge_삽입_테스트() {

        Menu menuToDetach = entityManager.find(Menu.class, 2); // 영속상태
        entityManager.detach(menuToDetach); // 준영속 상태

        menuToDetach.setMenuCode(999); // 데이터변경 (ID)
        menuToDetach.setMenuName("수박죽"); // 데이터변경

        // 있으면 병합하고, 없으면 새로 만든다.
        entityManager.merge(menuToDetach);

        // 새로운 데이터 조회
        Menu mergedMenu = entityManager.find(Menu.class, 999);
        assertEquals("수박죽", mergedMenu.getMenuName());

    }
}
