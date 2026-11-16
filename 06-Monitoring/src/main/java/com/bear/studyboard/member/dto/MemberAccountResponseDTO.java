package com.bear.studyboard.member.dto;

import com.bear.studyboard.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberAccountResponseDTO {

    private long id;
    private String email;
    private String nickname;
    private LocalDateTime joinedAt;
    private LocalDateTime withdrawnAt;

    public static MemberAccountResponseDTO from(Member member) {
        LocalDateTime joinedAt = member.getJoinedAt();
        LocalDateTime withdrawnAt = member.getWithdrawnAt();
        return new MemberAccountResponseDTO(
                member.getId(), member.getEmail(), member.getNickname(), joinedAt, withdrawnAt);
    }
}
