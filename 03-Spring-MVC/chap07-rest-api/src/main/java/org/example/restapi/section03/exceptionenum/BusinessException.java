package org.example.restapi.section03.exceptionenum;

// 비즈니스 규칙 위반을 표현하는 공통 사용자 정의 예외
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    // ErrorCode의 기본메시지를 활용하는 예외 생성자
    // 에러코드만
    public BusinessException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    // 같은 오류코드에 다른 상세내용을 적고싶을때 사용하는 생성자
    // 에러코드와 메세지 같이
    public BusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
