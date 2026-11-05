package com.practice;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Practice {

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
   // @Disabled("문제 01: 실패=> @Entitn(name= "practice_entity_name_member")로 지정해서 "Member가 아님 그래서 같지 않다.")

    public void 문제01_엔티티명은_클래스명과_같을까() {
        Entity entity = EntityNameMember.class.getAnnotation(Entity.class);

        assertEquals("Member", entity.name());
        // 엔티티.이름이 Member이니?
    }


    @Test
    //@Disabled("문제 02 실패 => nickname은 컬럼으로 만들어지지도, 저장되지도 않아서 조회하면 null이 나옴")
    public void 문제02_트랜지언트_필드도_데이터베이스에서_조회될까() {
        ColumnMember member = new ColumnMember();  // member 객체 생성
        member.setMemberNo(201); // member에 객체 저장 no 201번
        member.setMemberId("user201"); // 객체 id 저장 " user201
        member.setMemberPwd("pass201"); // 갹체 pwd 저장 " pass201
        member.setNickname("홍길동"); // 객체 nickname 저장 " 홍길동
        member.setAddress("서울시 종로구");  // 객체 address 저장 " 서울시 종로구

        EntityTransaction entityTransaction = entityManager.getTransaction() ;  // 트렌직션 등록
        entityTransaction.begin(); // 트렌젝션 시작
        entityManager.persist(member); // 엔티티메니저. persist에 메니저 등록
        entityTransaction.commit(); // 트렌젝션 종료
        entityManager.clear(); // 엔티티메니저. clear에 메니저 등록

        // foundMember에 앤티티메니저.find 컬럼맴버.clss 201번
        ColumnMember foundMember = entityManager.find(ColumnMember.class, 201);

        // foundMember의 nickname이 "홍길동"과 같은지 확인
        assertEquals("홍길동", foundMember.getNickname());
    } // 실패

    @Test
   // @Disabled("문제 03 성공 => persist() 즉시 INSERT가 실행돼서 번호가 생긴다.")
    public void 문제03_아이덴티티_전략은_persist_직후_기본키가_생길까() {
        IdentityMember member = new IdentityMember();
        member.setMemberId("user301");

        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.persist(member);

        // getMemberNo가 0보다 큰지 확인
        assertTrue(member.getMemberNo() > 0);


        entityTransaction.commit();
    } // 성공

    @Test
   // @Disabled("문제 04 성공 => @Enumerated(EnumType.STRING)이라 DB에 "AKMIN"이 저장돼여 이름으로 저장된다.)
    public void 문제04_문자열_방식의_열거형은_이름으로_저장될까() {
        EnumMember member = new EnumMember();
        member.setMemberNo(401);
        member.setMemberId("user401");
        member.setMemberRole(RoleType.ADMIN);

        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.persist(member);
        entityTransaction.commit();
        entityManager.clear();

        Object storedRole = entityManager.createNativeQuery(
                        "SELECT member_role "
                                +
                                "FROM tbl_practice_enum_member" +
                                " WHERE" +
                                " member_no = 401")
                .getSingleResult();

        assertEquals("ADMIN", storedRole);
    } // 통과

    @Test
   //@Disabled("문제 05 실패 => PropertyAccessMember에는 @Access(AccessType.PROPERTY)가 붙어 있어서 값이 FIELF가 아니다 / geeter/setter로 접근한다..")
    public void 문제05_프로퍼티_접근_엔티티는_필드로_접근할까() {
        Access access = PropertyAccessMember.class.getAnnotation(Access.class);

        assertEquals(AccessType.FIELD, access.value());
    }
}
