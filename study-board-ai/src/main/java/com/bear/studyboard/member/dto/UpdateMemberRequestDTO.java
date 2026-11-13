package com.bear.studyboard.member.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateMemberRequestDTO {

    @ToString.Exclude
    private String currentPassword;
    private String email;
    private String nickname;
}
