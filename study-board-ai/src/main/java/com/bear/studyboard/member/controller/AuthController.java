package com.bear.studyboard.member.controller;

import com.bear.studyboard.common.dto.ApiResponse;
import com.bear.studyboard.member.dto.LoginMemberRequestDTO;
import com.bear.studyboard.member.dto.MemberAccountResponseDTO;
import com.bear.studyboard.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    @PostMapping({"/auth/login", "/members/login"})
    public ResponseEntity<ApiResponse<MemberAccountResponseDTO>> login(@RequestBody LoginMemberRequestDTO request) {
        MemberAccountResponseDTO member = memberService.login(request);
        ApiResponse<MemberAccountResponseDTO> response = ApiResponse.of(HttpStatus.OK, "비밀번호가 일치합니다.", member);
        return ResponseEntity.ok(response);
    }
}
