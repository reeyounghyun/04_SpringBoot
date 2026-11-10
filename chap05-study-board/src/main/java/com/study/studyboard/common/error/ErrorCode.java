package com.study.studyboard.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "회원을 찾을 수 없습니다."),
    INVALID_USER_NUMBER(HttpStatus.BAD_REQUEST, "INVALID_USER_NUMBER", "이미 사용중인 회원 이름입니다."),
    DUPLICATE_USER_NAME(HttpStatus.CONFLICT, "DUPLICATE_USER_NAME", "이미 사용중인 회원 이름입니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버에서 요청을 처리하지 못했습니다.");

    private final HttpStatus httpStatus;
    private final String Code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        Code = code;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return Code;
    }

    public String getMessage() {
        return message;
    }
}
