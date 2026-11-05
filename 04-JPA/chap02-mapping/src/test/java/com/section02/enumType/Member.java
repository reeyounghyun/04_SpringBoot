package com.section02.enumType;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

/*
* Entity : 데이터 테이블과 매핑되는 자바 클래스
* */
@Entity(name="member_section02") // 수정: member_section01 → member_section02 (section01의 Member와 엔티티 이름 중복 방지)
@Table(name="tbl_member_section02") // 매핑될 테이블 작성 (수정: tbl_menu → tbl_member_section02)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Member {
    @Id
    @Column(name="member_no")
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
    @Enumerated(EnumType.ORDINAL)
//    @Enumerated(EnumType.STRING)
    private RoleType memberRole;

    @Column(name="status")
    private String status;
}
