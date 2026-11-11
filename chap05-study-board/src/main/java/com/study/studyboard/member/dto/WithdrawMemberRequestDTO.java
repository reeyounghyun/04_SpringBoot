package com.study.studyboard.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@NotBlank
public class WithdrawMemberRequestDTO {
    private String currentPassword;
}
