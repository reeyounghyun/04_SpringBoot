package com.section01.lifecycle;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lifecycle.TestClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class A_EntityManagerLifeCycleTests {

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /*테스트 코드는 다음과 같이 나누어 작성
    1. given:  테이스를 진행하기 위해 준비작업 (전제조건) 수행
    2. when: 테스트의 핵심로직(행동 또는 메서드 ) 수행
    3. then : 테스트의 결과를 검증
    */
    @Test
    public void 테스트_메소드() {

        // given
        TestClass testClass = new TestClass();
        String str1 = "첫번째 문자";
        String str2 = "두번째 문자";

        //when
        String rusult = testClass.sampleTest(str1, str2);
        //then
        Assertions.assertNotNull(rusult);

    }
}
