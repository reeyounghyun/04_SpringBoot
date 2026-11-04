package org.example.restapi.section03.exceptionenum;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*
* exceptionenum 패키지의 컨트롤러에서 발생한 예외를 한 곳에서 처리하는 클래스
* - 컨트롤러에서 BusinessException이 발생하면 이 클래스가 받아서 처리
* - 예외에 담긴 ErrorCode로 상태코드와 ErrorResponse(응답 본문)를 만들어 클라이언트에 반환
* - basePackages : 지정한 패키지의 컨트롤러에서 발생한 예외만 처리
* */
@RestControllerAdvice(basePackages = "org.example.restapi.section03.exceptionenum")
public class EnumExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException businessException) {

        // ErrorCode 꺼내기
        ErrorCode errorCode = businessException.getErrorCode();

        // ErrorResponse 만들기
        ErrorResponse response = ErrorResponse.from(businessException);
        //  타입         변수명       객체를 만들어 주는 매서드

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }
}
