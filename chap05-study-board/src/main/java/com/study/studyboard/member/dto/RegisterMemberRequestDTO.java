package com.study.studyboard.member.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegisterMemberRequestDTO {

    private String email;
    private String nickname;

    // 비밀번호가 노출되면 안되서 어노테이션 추가해줌.
    @ToString.Exclude
    private String password;
}
