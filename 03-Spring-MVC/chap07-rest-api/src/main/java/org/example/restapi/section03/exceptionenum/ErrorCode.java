package org.example.restapi.section03.exceptionenum;

import org.springframework.http.HttpStatus;

// 어플리케이션에서 사용할 예외 정보를 관리하는 Enum
// 예외를 정의하고, 예외메시지를 일관성있게 전달하는 목적이다.
// enum은 정해진 값 목록을 미리 정의해 두는 문법입니다. 여기서는 그 목록에 예외 정보를 담아 둔 것입니다.
public enum ErrorCode {

    // 추후 필요한 예외가있으면 정의하서 추가만 하면된다.
    // 예외 상수 목록 : 상수명(상태코드, 에러코드, 메시지) → 아래 생성자가 호출됨
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "회원을 찾을 수 없습니다."),
    INVALID_USER_NUMBER(HttpStatus.BAD_REQUEST, "INVALID_USER_NUMBER", "이미 사용중인 회원 이름입니다."),
    DUPLICATE_USER_NAME(HttpStatus.CONFLICT, "DUPLICATE_USER_NAME", "이미 사용중인 회원 이름입니다.");

    private final HttpStatus httpStatus; // HTTP 상태코드 (예: HttpStatus.BAD_REQUEST → 400)
    private final String code;           // 애플리케이션에서 정의한 에러 코드 (예: "USER_NOT_FOUND")
    private final String message;        // 클라이언트에게 전달할 에러 메시지

    // 생성자 : enum 상수를 만들 때 상태코드, 에러코드, 메시지를 전달받아 필드에 저장
    // enum의 생성자는 자동으로 private (외부에서 new로 만들 수 없음)

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
