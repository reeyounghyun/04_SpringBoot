package com.section01.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class PropertyAccessTest {

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
    public void 테이블_만들기_테스트() {
        // given
        Member member = new Member();
        member.setMemberNo(1);
        member.setMemberId("user01");
        member.setMemberPwd("user01");
        member.setNickname("bear");

        // when
        entityManager.persist(member); // 영속 상태


        Member foundMember = entityManager.find(Member.class, member.getMemberNo());
        Assertions.assertEquals(member.getMemberId(), foundMember.getMemberId());
    }
}
