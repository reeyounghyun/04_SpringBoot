package org.example.restapi.section03.exceptionenum;

/*
* 예외가 발생했을대 클라이언트에 전달할 응답 DTO*/
public class ErrorResponse {

    private int status;
    private String code;
    private String message;
    private String detail;

    public ErrorResponse(int status, String code, String message, String detail) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.detail = detail;
    }

    // BusinessException 가진 Error코드와 상세메시지로 응답 객체 생성하는 펙토리 메서드
    public static ErrorResponse from(BusinessException exception) {
        ErrorCode errorCode = exception.getErrorCode();

        return new ErrorResponse(
                errorCode.getHttpStatus().value(),  // status  : HTTP 상태코드 숫자 (예: 404)
                errorCode.getCode(),                // code    : 에러 코드 (예: "USER_NOT_FOUND")
                errorCode.getMessage(),             // message : ErrorCode에 정의한 기본 메시지
                exception.getMessage()              // detail  : 예외 생성 시 전달한 상세 메시지 (없으면 null)
        );
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDetail() {
        return detail;
    }
}
