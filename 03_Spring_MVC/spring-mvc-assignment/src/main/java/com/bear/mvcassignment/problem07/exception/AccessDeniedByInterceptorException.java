package com.bear.mvcassignment.problem07.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 문제 7-1. 사용자 정의 예외 만들기 (수업: MemberRegistException)
 * <p>
 * 1. RuntimeException을 상속한다.
 * 2. String message를 받는 생성자를 만들고 super(message)로 부모에 메시지를 저장한다.
 */
public class AccessDeniedByInterceptorException extends RuntimeException {
    public AccessDeniedByInterceptorException(String message) {
        super(message);
    }
}
