package com.study.studyboard.member.dto;


import com.study.studyboard.common.dto.ApiResponse;
import com.study.studyboard.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
// 회원가입, 회원 탈퇴
// 응답에 사용할 DTO
public class MemberAccountResponseDTO {

    private long id;
    private String email;
    private String nickname;
    private LocalDateTime joinedAt;
    private LocalDateTime withdrawnAt;

    // Entity -> DTO
    public static MemberAccountResponseDTO from(Member member) {
        return new MemberAccountResponseDTO(
                member.getId(),
                member.getEmail(),
                member.getNickname(),
                member.getJoinAt(),
                member.getDrawnAt() == null ? null : member.getDrawnAt());
    }
}
