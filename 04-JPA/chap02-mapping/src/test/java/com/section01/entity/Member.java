package com.section01.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;
/*
* Entity : 데이터 테이블과 매핑되는 자바 클래스
* */
@Entity(name="member_section01")
@Table(name="tbl_member_section01") // 매핑될 테이블 작성
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Member {
    @Id
    @Column(name="member_no")
    /*
    * @GeneratedValue : 기본키(PK) 값을 직접 넣지 않고 자동으로 생성하는 설정
    * - IDENTITY : DB에 생성을 맡김 (MySQL의 AUTO_INCREMENT, 1,2,3... 자동 증가)
    * - SEQUENCE : DB의 시퀀스 객체로 번호를 생성 (Oracle, PostgreSQL 등에서 사용)
    * - TABLE    : 번호를 세는(count) 별도 테이블을 만들어 생성
    * - AUTO     : DB에 맞는 방식을 JPA가 자동 선택 (default)
    */
    @GeneratedValue(strategy = GenerationType.TABLE) // IDENTITY MYSQL에서 AutoIncrement를 하겠다
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
}
