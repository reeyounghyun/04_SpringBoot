package com.practice;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class Practice {

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /*
    @BeforeAll
    - 해당 어노테이션이 붙은 메소드는 전체 테스트가 실행되기 전 딱한번만 실행된다.
    - 싱글턴 패턴을 적용하거나, 리소스가 큰 작업을 수행할때
    * */
    @BeforeAll
    public static void initFactory() {
        /*
        * entityManagerFactory 엔티티 매니저 팩토리
        * 1. persistence.xml 설정을 읽어 데이터베이스 연결 정보 및 엔티티 클래스 목록을 파악한다.
        * 2. 메타 데이터 처리: 엔티티 클래스 구조와 관계 정보를 저장
        * 3. 커넥션 풀 초기화: 데이터베이스 연결을 위해 내부 커넥션 풀을 설정 및 초기화
        * 4. 캐시 초기화: 성능 최적화를 위한 (1차 캐시, 쿼리 캐시) 초기화
        * */
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    // 테스트 메서드가 각각이 호출될때 이전에 동작
    @BeforeEach
    public void initManager() {
        entityManager = entityManagerFactory.createEntityManager(); // 엔티티 매니저 생성
    }

    @AfterAll
    public static void closeFactory() {
        entityManagerFactory.close();
    }

    @AfterEach
    public void closeManager() {
        entityManager.close(); // 엔티티 매니저 종료
    }

    // @Disabled("성공/실패를 예측한 뒤 이 어노테이션을 제거하고 실행하세요.")
    @Test
    void 문제01_같은_식별자로_두_번_조회하면_같은_객체일까() {
        Menu firstMenu = entityManager.find(Menu.class, 2);
        Menu secondMenu = entityManager.find(Menu.class, 2);

        assertSame(firstMenu, secondMenu);
    } // 성공

    //@Disabled("성공/실패를 예측한 뒤 이 어노테이션을 제거하고 실행하세요.")
    @Test
    void 문제02_persist하면_엔티티는_영속_상태가_될까() {
        Menu newMenu = createMenu(9001, "연습 메뉴 1");
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();
        entityManager.persist(newMenu);

        assertTrue(entityManager.contains(newMenu));
    } // 성공

    //@Disabled("성공/실패를 예측한 뒤 이 어노테이션을 제거하고 실행하세요.")
    @Test
    void 문제03_영속_엔티티의_값만_바꾸면_변경을_감지할까() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Menu foundMenu = entityManager.find(Menu.class, 3);
        String changedName = "변경 감지 메뉴";

        foundMenu.setMenuName(changedName);
        entityManager.flush(); // 쿼리를 실행
        entityManager.clear(); // 영속성 컨텍스트를 초기화

        assertEquals(changedName, entityManager.find(Menu.class, 3).getMenuName());
    } // 성공

   // @Disabled("성공/실패를 예측한 뒤 이 어노테이션을 제거하고 실행하세요.")
    @Test
    void 문제04_remove한_엔티티를_같은_컨텍스트에서_다시_조회할_수_있을까() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Menu foundMenu = entityManager.find(Menu.class, 3);

        entityManager.remove(foundMenu);

        assertNull(entityManager.find(Menu.class, 3));
    } // 성공

   // @Disabled("성공/실패를 예측한 뒤 이 어노테이션을 제거하고 실행하세요.")
    @Test
    void 문제05_merge를_호출하면_준영속_객체_자체가_다시_영속_상태가_될까() {
        Menu detachedMenu = entityManager.find(Menu.class, 3);
        entityManager.detach(detachedMenu);
        detachedMenu.setMenuName("병합 연습 메뉴");

        Menu mergedMenu = entityManager.merge(detachedMenu);

        assertTrue(entityManager.contains(detachedMenu)); // 준영속상태 실패
        assertTrue(entityManager.contains(mergedMenu)); // 성공
    }

    private Menu createMenu(int menuCode, String menuName) {
        return new Menu(menuCode, menuName, 10000, 1, "Y");
    }
}


