package org.example.restapi.testpractice.testsection05;

import org.springframework.http.ResponseEntity;

/*
 * Swagger 문서용 인터페이스
 * - 여기에는 문서 설명 어노테이션만 달아요.
 * - 실제 동작은 MemberSwaggerControllerImpl이 구현해요.
 *
 * [문제 1] 인터페이스에 @Tag를 붙이세요.
 *  - name        : "회원 관리 API (연습)"
 *  - description : "Section #5 연습문제 - 회원 CRUD"
 * */
public interface MemberSwaggerController {

    // [문제 2-1] 회원 전체 조회
    //  - summary: "회원 전체 조회"
    //  - 200 "조회 성공" (MemberResponseMessage 스키마)
    ResponseEntity<MemberResponseMessage> findAllMembers();

    // [문제 2-2] 회원 상세 조회
    //  - summary: "회원 상세 조회"
    //  - 200 "조회 성공" (MemberResponseMessage 스키마) / 404 "회원 없음" (hidden)
    //  - @Parameter: memberNo, "조회할 회원 번호", 필수
    ResponseEntity<MemberResponseMessage> findMemberByNo(int memberNo);

    // [문제 2-3] 회원 등록
    //  - summary: "회원 등록"
    //  - 201 "등록 성공" (hidden) / 400 "잘못된 요청" (hidden)
    ResponseEntity<Void> registMember(MemberDTO newMember);

    // [문제 2-4] 회원 정보 수정
    //  - summary: "회원 정보 수정"
    //  - 200 "수정 성공" (hidden) / 404 "회원 없음" (hidden)
    //  - @Parameter: memberNo, "수정할 회원 번호", 필수
    ResponseEntity<Void> modifyMember(int memberNo, MemberDTO modifyInfo);

    // [문제 2-5] 회원 삭제
    //  - summary: "회원 삭제"
    //  - 204 "삭제 성공" (hidden) / 404 "회원 없음" (hidden)
    //  - @Parameter: memberNo, "삭제할 회원 번호", 필수
    ResponseEntity<Void> removeMember(int memberNo);
}
