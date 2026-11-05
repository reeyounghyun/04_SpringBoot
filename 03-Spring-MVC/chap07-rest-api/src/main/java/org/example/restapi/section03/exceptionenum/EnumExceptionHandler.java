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

        //  Response (리스폰스) │ 응답 │ 서버 → 클라이언트 ("여기 있습니다")
        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
        // 리턴 응답Entity.스테이터스(에러코드.get(가져와)  HttpStatus(상태코드)필드 값을 꺼내라. 어디에?
        // => 상태코드 칸에는 status(404), 본문 칸에는 body(response) == [ErrorResponse]객체

        // 응답을 만들어서 반환한다
        // 상태코드 칸 = 에러코드에서 꺼낸 상태코드(예: 404)
        // 본문(body) 칸 = response(ErrorResponse 객체)
    }
}
