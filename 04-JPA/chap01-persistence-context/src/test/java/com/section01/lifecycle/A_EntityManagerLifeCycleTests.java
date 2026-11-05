package com.section01.lifecycle;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lifecycle.TestClass;
import org.junit.jupiter.api.*;

public class A_EntityManagerLifeCycleTests {

    //EntityManagerFactory 가 "싱글톤" 한개 만들어놓고 재사용 하겠다.
    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /*
     @BeforeAll
     - 해당 어노테이션이 붙은 메소드는 전체 테스트가 실행되기 전 딱한번만 실행된다.
     - 싱글턴 패턴을 적용하거나, 리소스가 큰 작업을 수행할때
    * */
    @BeforeAll // 전체 시작 전에 1번 실행돼서 ///  첫번째로 동작.
    public static void initFactory() {
        /*
         * entityManagerFactory 엔티티 매니저 팩토리
         * 1. persistence.xml 설정을 읽어 데이터베이스 연결 정보 및 엔티티 클래스 목록을 파악한다.
         * 2. 메타 데이터 처리: 엔티티 클래스 구조와 관계 정보를 저장
         *  => "엔티티 클래스 구조"는 Menu 클래스가 DB 테이블과 어떻게 연결되는지에 대한 정보를 말해요.
         *      팩토리를 만들 때 JPA가 Menu 클래스에 붙은 어노테이션을 읽고, 이 정보를 정리해서 저장해 둡니다.
         *      이 정보를 메타데이터(데이터에 대한 데이터)라고 불러요.
         *
         * 3. 커넥션 풀 초기화: 데이터베이스 연결을 위해 내부 커넥션 풀을 설정 및 초기화
         * 4. 캐시 초기화: 성능 최적화를 위한 (1차 캐시, 쿼리 캐시) 초기화
         * */
        entityManagerFactory = Persistence.createEntityManagerFactory("jpabook");
    }

    /*JUnit의 실행 규칙으로 @Test 개수만큼 반복한다.*/
    @BeforeEach // 테스트 전(테스트 메소드가 각각 호출되기 이전)에 동작, 두번째로 동작함
    public void initManager() {
        entityManager = entityManagerFactory.createEntityManager(); // 엔티티 매니저 생성
    }

    @AfterAll // 전체 끝난 뒤에 1번 동작,  마지막 다섯번째로 동작함
    public static void closeFactory() {
        entityManagerFactory.close();
    }

    @AfterEach // 테스트 후에 동작.  네번째로 동작함
    public void closeManager() {
        entityManager.close(); // 엔티티 매니저 종료
    }


    /*테스트 코드는 다음과 같이 나누어 작성
    1. given:  테스트를 진행하기 위해 준비작업 (전제조건) 수행
    2. when: 테스트의 핵심로직(행동 또는 메서드 ) 수행
    3. then : 테스트의 결과를 검증
    */
    @Test // 테스트 본문 실행 // 세번째로 동작
    public void 테스트_메소드() {

        // given
        TestClass testClass = new TestClass();
        String str1 = "첫번째 문자";
        String str2 = "두번째 문자";

        //when
        String result = testClass.sampleTest(str1, str2);
        //then
        Assertions.assertNotNull(result);

    }

    /*
    * hashCode()
    *  객체마다 붙는 번호(정수)**를 돌려주는 메소드예요
    *  모든 Java 객체가 Object 클래스로부터 물려받는다.
    */
    @Test
    public void 엔티티매니저_팩토리_생성_확인1() {
        System.out.println("entityManagerFactory.hashCode() = " + entityManagerFactory.hashCode());
        System.out.println("entityManager.hashCode() = " + entityManager.hashCode());
    }

    @Test
    public void 엔티티매니저_팩토리_생성_확인2() {
        System.out.println("entityManagerFactory.hashCode() = " + entityManagerFactory.hashCode());
        System.out.println("entityManager.hashCode() = " + entityManager.hashCode());
    }
}
