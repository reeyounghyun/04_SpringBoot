package com.study.studyboard.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
// ErrorCode: 서비스들이 가져다 쓰는 도구
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    INVALID_USER_NUMBER(HttpStatus.BAD_REQUEST, "이미 사용중인 회원 이름입니다."),
    DUPLICATE_USER_NAME(HttpStatus.CONFLICT, "이미 사용중인 회원 이름입니다."),

    //Email이 없어서 만들어줌
    MEMBER_EMAIL_DUPLICATE(HttpStatus.CONFLICT, "이미 등록된 이메일 입니다."),

    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 요청을 처리하지 못했습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "요청 입력을 확인해주세요"),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."),
    MEMBER_WITHDRAWN(HttpStatus.CONFLICT, "탈퇴한 회원입니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    MEMBER_ALREADY_WITHDRAWN(HttpStatus.CONFLICT, "이미 탈퇴한 회원입니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지않는 게시글 입니다.");


    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }



}

/*
*
* 400 BAD_REQUEST: 요청 형식이 잘못됨. 예: 필수 값이 비었음
401 UNAUTHORIZED: 본인 확인 실패. 예: 비밀번호 틀림
404 NOT_FOUND: 찾는 대상이 없음. 예: 회원, 게시글, 분류가 없음
409 CONFLICT: 지금 상태와 부딪힘. 예: 이메일 중복, 이미 탈퇴함
500 INTERNAL_SERVER_ERROR: 서버 내부 문제. 예: 예상 못 한 에러
*
* */
