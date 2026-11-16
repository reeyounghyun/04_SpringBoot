package com.study.studyboard.member.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
// 로그인 요청에 사용할 DTO
public class LoginMemberRequestDTO {

    private String email;

    // 비밀번호가 로그에 노출되지 않도록 toString()에서 제외
    @ToString.Exclude
    private String password;
}
