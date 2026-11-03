package com.bear.mvcassignment.problem07.exception;

import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 문제 7-5. 전역 예외처리 (수업: GlobalExceptionHandler)
 * <p>
 * 1. 이 클래스를 @ControllerAdvice로 등록한다.
 * 2. AccessDeniedByInterceptorException을 처리하는 메서드를 만든다.
 * - @ExceptionHandler(AccessDeniedByInterceptorException.class)를 붙인다.
 * - @ResponseStatus(HttpStatus.FORBIDDEN)를 붙여 응답 상태 코드를 403으로 지정한다.
 * - 콘솔에 "[GlobalExceptionHandler] 인터셉터 예외 처리" 로그를 출력한다.
 * - Model에 exception을 담고 논리 뷰 이름 error/interceptor를 반환한다.
 * <p>
 * 확인 (과제: Interceptor에서 예외를 던졌을 때 ControllerAdvice의 ExceptionHandler로 해결이 되는가?)
 * - GET /interceptors/exception 요청 시 이 메서드가 실행되고 error/interceptor 화면이 보이는가?
 * - 개발자도구(F12) Network 탭에서 상태 코드가 403으로 오는가?
 * - @ResponseStatus를 지웠을 때 상태 코드는 몇으로 바뀌는가?
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccessDeniedByInterceptorException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String accessDeniedExceptionHandler(Model model, AccessDeniedByInterceptorException exception) {

        System.out.println("[GlobalExceptionHandler] 인터셉터 예외 처리");

        model.addAttribute("exception", exception);    // 2. model에 exception 담기
        return "error/interceptor";    // 3. "error/interceptor" 반환
    }
}