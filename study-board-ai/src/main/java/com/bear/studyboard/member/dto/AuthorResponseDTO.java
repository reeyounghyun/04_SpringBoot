package com.bear.studyboard.member.dto;

import com.bear.studyboard.member.entity.Member;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthorResponseDTO {

    private long id;
    private String nickname;

    public static AuthorResponseDTO from(Member member) {
        return new AuthorResponseDTO(member.getId(), member.getNickname());
    }
}
