package com.section02.crud;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class A_EntityManagerCRUDTest {

    //entityManagerFactory 가 "싱글톤" 한개 만들어놓고 재사용 하겠다.
    private static EntityManagerFactory entityManagerFactory; // 필드, 클래스 전체에 하나만 있음.
    private EntityManager entityManager;  // 인스턴스 필드로 테스트 객체마다 따로 있음

    /*
     @BeforeAll
     - 해당 어노테이션이 붙은 메소드는 전체 테스트가 실행되기 전 딱한번만 실행된다.
     - 싱글턴 패턴을 적용하거나, 리소스가 큰 작업을 수행할때
    * */
    /*
     * entityManagerFactory 엔티티 매니저 팩토리
     * 1. persistence.xml 설정을 읽어 데이터베이스 연결 정보 및 엔티티 클래스 목록을 파악한다.
     * 2. 메타 데이터 처리: 엔티티 클래스 구조와 관계 정보를 저장
     * 3. 커넥션 풀 초기화: 데이터베이스 연결을 위해 내부 커넥션 풀을 설정 및 초기화
     * 4. 캐시 초기화: 성능 최적화를 위한 (1차 캐시, 쿼리 캐시) 초기화
     * */
    @BeforeAll
    public static void initFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpabook");
    } // 동작 1

    // 테스트 메서드가 각각이 호출될때 이전에 동작
    @BeforeEach
    public void initManager() {
        entityManager = entityManagerFactory.createEntityManager();
    } // 엔티티 매니저 생성, 동작 2

    // 전체 3번 동작

    // 전체 끝난 뒤에 1번 동작,  마지막 다섯번째로 동작함
    @AfterAll
    public static void closeFactory() {
        entityManagerFactory.close();
    } // 동작 5


    // 테스트 후에 동작.  네번째로 동작함
    @AfterEach
    public void closeManager() {
        entityManager.close();
    } // 엔티티 매니저 종료, 동작 4

    @Test
    public void 새로운_메뉴_추가_테스트() {
        // given
        Menu menu = new Menu();
        menu.setMenuName("롤백시키기 위한 데이터");
        menu.setMenuPrice(5500);
        menu.setCategoryCode(4);
        menu.setOrderableStatus("Y");
        /*
         *  Persistence Context에 메뉴가 등록된다
         *  => 정정: 이 시점은 new로 객체만 만들고 값을 넣은 상태라 아직 등록 전(비영속 상태)이다.
         *     실제 등록은 아래 entityManager.persist(menu)를 호출할 때 된다. (영속 상태)
         *   menu.setMenuPrice(5500);
         *   menu.setCategoryCode(4);
         *   menu.setOrderableStatus("Y");
         * */
        // when
        EntityTransaction entityTransaction = entityManager.getTransaction(); // entityManager에서 트랜잭션객체 받기
        entityTransaction.begin(); // 트랜잭션 시작

        try {
            /*
             * persist()는 엔티티를 영속성 컨텍스트에 등록만 하고, insert 구문은 commit 시점까지 쓰기 지연 저장소에 모았다가, 한번에 내보낸다. 그런데,
             * GeneratedValue(strategy = GenerationType.IDENTITY) 전략에 의해 컨텍스트에 저장할 ID값이 필요하므로
             * 미리 알기위해 insert가 바로 실행된다.
             */
            entityManager.persist(menu);
            // 만든 객체를 영속성 컨텍스트에 등록함.
            //영속성 컨텍스트에 등록하기위해 미리 sql을 보낸다.

            entityTransaction.commit();
            // 트랜잭션에 commit => DB에 반영하게 된다.
            // 코드 흐름에서 commit을 하면 트랜잭션 커밋이 되고, 영속성 컨텍스트에 있는 모든 엔티티를 DB에 반영한다.

        } catch (Exception e) {

            entityTransaction.rollback();
            e.printStackTrace();
        }

        /*
         * contains : 해당 객체가 persistence context에 등록이 되어있는지 확인하는 메소드이다.
         * 있으면 true 없으면 false
         */
        // then
        // 영속성 컨텍스트에 반영이 되어있으니 true가 된다.
        assertTrue(entityManager.contains(menu));
    }

    @Test
    public void 메뉴코드로_메뉴_조회_테스트() {

        int menuCode = 2;

        // when
        //.find(엔티티 클래스, 식별자): 식별자로 엔티티 하나 조회
        // 찾아서 반환할 때 쓰인다.
        // menu 기반 id값으로 가져오게 된다.
        Menu foundMenu = entityManager.find(Menu.class, menuCode);

        Assertions.assertNotNull(foundMenu);
        //assertNotNull : null이 아니면 테스트 통과, null이면 테스트 실패
        // 한 번 걸러주기 위해

        // 찾아온 메뉴 데이터의 menuCode가 2인지?
        // int menuCode = 2;
        assertEquals(menuCode, foundMenu.getMenuCode());
        System.out.println("foundMenu = " + foundMenu);
    }

    // 영속성 컨텍스트 > 우럭스무디 메뉴 투 체인지 > 갈치스무디 찾아 > try에서 menuNameToChange를 이용해서 갈치스무디로 이름으로 변경
    @Test
    public void 메뉴_이름_수정_테스트() {

        Menu menuToChange = entityManager.find(Menu.class, 2);
        System.out.println("menu = " + menuToChange);

        String menuNameToChange = "갈치스무디";

        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();


        /*
        dirty checking
        변경감지
        영속 상태의 엔티티는 값만 바꾸면 update나 save 하지 않아도
        최초상태의 스냅샷을 기억했다가 커밋 시점에 달라진 값으로 데이터베이스에 적용할 update 구문을 작성해준다.
        변경해준다.
        *
        * */
        try {
            menuToChange.setMenuName(menuNameToChange);

            entityTransaction.commit();
        } catch (Exception e) {
            entityTransaction.rollback();
            e.printStackTrace();
        }

        // 같으면 패스 아니면?
        // 테스트 성공
        assertEquals(menuNameToChange, entityManager.find(Menu.class, 2).getMenuName());

    }

    @Test
    public void 메뉴_삭제하기_테스트() {

        // 영속성 컨텍스트에 menuToRemove가 객체 등록
        Menu menuToRemove = entityManager.find(Menu.class, 1);

        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin(); // 트랜잭션 시작

        try {
            entityManager.remove(menuToRemove); // 영속 상태에서 삭제 상태로 변경
            entityTransaction.commit(); // 삭제된 상태에서 데이터베이스에서 제거 구문 작성 및 전송
        } catch (Exception e) {
            entityTransaction.rollback();
            e.printStackTrace();
        }

        Menu removedMenu = entityManager.find(Menu.class, 1);
        assertNull(removedMenu); // 테스트 성공
    }
}
