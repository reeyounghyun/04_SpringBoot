package com.bear.studyboard.member.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginMemberRequestDTO {

    private String email;

    @ToString.Exclude
    private String password;
}
