package org.example.exceptionhandle.exception;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/*
@ControllerAdvice
* - 모든 컨트롤러에 적용되는 공통 코드를 모아놓는 공간
* - 여러 컨트롤러에서 발생한 예외를 공통 처리할 수 있는 클래스
* */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public String nullPointerExceptionHandler(NullPointerException exception) {

        System.out.println("controller 레벨의 exception 처리!!");

        return "error/nullPointer";
    }
}
