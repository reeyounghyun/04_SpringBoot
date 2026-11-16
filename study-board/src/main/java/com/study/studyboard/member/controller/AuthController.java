package com.study.studyboard.member.controller;

import com.study.studyboard.common.dto.ApiResponse;
import com.study.studyboard.member.dto.LoginMemberRequestDTO;
import com.study.studyboard.member.dto.MemberAccountResponseDTO;
import com.study.studyboard.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<MemberAccountResponseDTO>> login(@RequestBody LoginMemberRequestDTO request) {

        MemberAccountResponseDTO member = memberService.login(request);

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK, "비밀번호가 일치합니다.", member));
    }
}

