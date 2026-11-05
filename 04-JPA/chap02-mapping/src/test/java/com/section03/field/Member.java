package com.section03.field;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

/*
* Entity : 데이터 테이블과 매핑되는 자바 클래스
* */
@Entity(name="member_section03")
@Table(name="tbl_member_section03") // 매핑될 테이블 작성
@Setter

/*
* ID가 필드에 있으면 -> FIELD
* ID가 Getter에 있으면 -> PROPERTY
* 지금은 @Id가 memberNo 필드에 있어서 @Access를 빼도 FIELD로 동작한다.
* */
@Access(AccessType.FIELD)
//JPA가 엔티티의 값을 필드를 통해 접근한다. (기본값 FIELD)
// JPA가 엔티티의 값을 필드에 직접 접근해서 읽고 씀 (@Id가 필드에 있으면 기본이 FIELD)

// @Access(AccessType.PROPERTY)
// JPA가 엔티티의 값을 필드에 직접 접근해서 읽고 씀
// @Access(AccessType.PROPERTY) // JPA가 엔티티의 값을 getter/setter를 통해 읽고 씀

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Member {
    @Id
    @Column(name="member_no")
    /*
    * SEQUENCE : DB에 있는 시퀀스 객체 (Oracle, PostgreSQL)
    * Table : 키생성 전용 테이블 -> 기본 키값을 관리
    * AUTO : DB에 맞는 전략을 자동으로 선택
    * */
   // @GeneratedValue(strategy = GenerationType.TABLE)
    private int memberNo;

    @Column(name="member_id")
    private String memberId;

    @Column(name="member_pwd")
    private String memberPwd;

    @Column(name="nickname")
    private String nickname;

    @Column(name="phone")
    private String phone;

    @Column(name="email")
    private String email;

    @Column(name="address")
    private String address;

    @Column(name="enroll_date")
    private Date enrollDate;

    @Column(name="member_role")
    private String memberRole;

    @Column(name="status")
    private String status;

    /*
    * @Access(AccessType.FIELD)라서 JPA는 이 getter를 쓰지 않고 필드에 직접 접근합니다.
    * 그래서 DB에는 "getter에서 동작함..."이 붙지 않은 원래 값이 저장돼요.
    * 이 getter는 필드 접근과 프로퍼티 접근의 차이를 확인하려는 예제로 보입니다.
    * */
    public String getMemberId() {
        return "getter에서 동작함..." + memberId;
    }

    public String getMemberPwd() {
        return "getter에서 동작함..." + memberPwd;
    }

    public String getNickname() {
        return "getter에서 동작함..." + nickname;
    }

}
