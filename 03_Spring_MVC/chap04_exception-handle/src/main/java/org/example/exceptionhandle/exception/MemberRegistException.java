package org.example.exceptionhandle.exception;


// 사용자정의 예외
public class MemberRegistException extends Exception {

    // 부모로 예외 메시지 저장 가능!
    public MemberRegistException(String message) {
        super(message);
    }
}
