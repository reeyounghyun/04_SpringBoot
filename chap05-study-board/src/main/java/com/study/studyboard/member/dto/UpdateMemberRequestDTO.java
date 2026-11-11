package com.study.studyboard.member.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateMemberRequestDTO {
    private String email;
    private String nickname;

}
