package com.study.studyboard.member.controller;


import com.study.studyboard.common.dto.ApiResponse;
import com.study.studyboard.member.dto.MemberAccountResponseDTO;
import com.study.studyboard.member.dto.RegisterMemberRequestDTO;
import com.study.studyboard.member.dto.UpdateMemberRequestDTO;
import com.study.studyboard.member.dto.WithdrawMemberRequestDTO;
import com.study.studyboard.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
// 회원 기능만 모아둔 패키지
public class MemberController {
    // 대문자로 시작 : 클래스나 타입
    // 소문자로 시작 : 변수나 매소드
    private final MemberService memberService;


    //    접근제어자 반환형 메서드명(매개변수){
//
//    }
    // 회원가입
    @PostMapping
    // register 는 MemberController에서 사용하고
    // register 는 MemberService에서 부름
    public ResponseEntity<ApiResponse<MemberAccountResponseDTO>> register(
            @RequestBody RegisterMemberRequestDTO request
    ) {

        // memberService의 register 메서드를 호출하면서 request를 넘겨준다.
        // 그 메서드가 회원가입을 처리하고 DTO 객체를 돌려준다
        // 돌려받은 객체를 member 변수에 담는다.
        MemberAccountResponseDTO member = memberService.register(request);

        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity
                .status(status)
                .body(
                        ApiResponse.of(status, "회원 등록 성공!", member)
                );
    }

    // localhost:8080/api/members/5
    // localhost:8080/api/members/{memberId}
    // 회원 정보 수정
    @PatchMapping("/{memberId}")
    public ResponseEntity<ApiResponse<MemberAccountResponseDTO>> updateMember
    (@PathVariable Long memberId,
     @RequestBody UpdateMemberRequestDTO updateMemberRequestDTO
    ) {

        MemberAccountResponseDTO updateMember = memberService.updateMember(memberId, updateMemberRequestDTO);

        HttpStatus updateStatus = HttpStatus.OK;

        return ResponseEntity
                .status(updateStatus)
                .body(ApiResponse.of(HttpStatus.OK, "회원정보수정이 잘됐습니다~!", updateMember));
//        return ResponseEntity
//                .ok(ApiResponse.of(HttpStatus.OK, "회원정보수정이 잘됐습니다~!", updateMember));
    }

    // 회원 탈퇴
    @PostMapping("/{memberId}/withdrawal")
    public ResponseEntity<ApiResponse<MemberAccountResponseDTO>> withdrawMember
    (@PathVariable Long memberId,
     @RequestBody WithdrawMemberRequestDTO request
     
    ){
        MemberAccountResponseDTO member = memberService.withdrawMember(memberId, request);

        HttpStatus withdrewMember = HttpStatus.OK;

        return ResponseEntity
                .status(withdrewMember)
                .body(ApiResponse.of(HttpStatus.OK, "회원탈퇴가 완료되었습니다.", member));
    }


}
