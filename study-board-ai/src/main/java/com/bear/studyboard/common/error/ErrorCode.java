package com.bear.studyboard.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "요청 입력을 확인해 주세요."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    MEMBER_EMAIL_DUPLICATE(HttpStatus.CONFLICT, "이미 등록된 이메일입니다."),
    MEMBER_WITHDRAWN(HttpStatus.CONFLICT, "탈퇴한 회원입니다."),
    INVALID_MEMBER_PASSWORD(HttpStatus.FORBIDDEN, "현재 비밀번호를 확인해 주세요."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "수업자료를 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "수업 분류를 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 요청을 처리하지 못했습니다."),
    AI_REQUEST_FAILED(HttpStatus.BAD_GATEWAY, "AI 서비스 요청에 실패했습니다."),
    AI_RESPONSE_INVALID(HttpStatus.BAD_GATEWAY, "AI 응답을 사용할 수 없습니다."),

    QUIZ_NOT_FOUND(HttpStatus.NOT_FOUND, "문제를 찾을 수 없습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
