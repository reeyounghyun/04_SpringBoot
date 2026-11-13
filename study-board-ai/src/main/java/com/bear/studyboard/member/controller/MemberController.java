package com.bear.studyboard.member.controller;

import com.bear.studyboard.common.dto.ApiResponse;
import com.bear.studyboard.member.dto.MemberAccountResponseDTO;
import com.bear.studyboard.member.dto.RegisterMemberRequestDTO;
import com.bear.studyboard.member.dto.UpdateMemberRequestDTO;
import com.bear.studyboard.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<ApiResponse<MemberAccountResponseDTO>> register(
            @RequestBody RegisterMemberRequestDTO request
    ) {
        MemberAccountResponseDTO member = memberService.register(request);
        HttpStatus status = HttpStatus.CREATED;
        ApiResponse<MemberAccountResponseDTO> response = ApiResponse.of(status, "회원으로 등록했습니다.", member);
        return ResponseEntity.status(status).body(response);
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<ApiResponse<MemberAccountResponseDTO>> updateMember(
            @PathVariable long memberId,
            @RequestBody UpdateMemberRequestDTO request
    ) {
        MemberAccountResponseDTO member = memberService.updateMember(memberId, request);
        ApiResponse<MemberAccountResponseDTO> response = ApiResponse.of(HttpStatus.OK, "회원 정보를 수정했습니다.", member);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{memberId}/withdrawal")
    public ResponseEntity<ApiResponse<MemberAccountResponseDTO>> withdrawMember(
            @PathVariable long memberId,
            @RequestBody String currentPassword
    ) {
        MemberAccountResponseDTO member = memberService.withdrawMember(memberId, currentPassword);
        ApiResponse<MemberAccountResponseDTO> response = ApiResponse.of(HttpStatus.OK, "탈퇴 처리했습니다.", member);
        return ResponseEntity.ok(response);
    }
}
