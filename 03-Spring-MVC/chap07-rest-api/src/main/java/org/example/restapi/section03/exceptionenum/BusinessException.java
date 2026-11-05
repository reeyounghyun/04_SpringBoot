package org.example.restapi.section03.exceptionenum;

// 비즈니스 규칙 위반을 표현하는
// 공통 사용자 정의 예외
public class BusinessException extends RuntimeException {

    // 이 클레스에서는 사용하면서 다른 클레스에서는 getter로 접근이 가능하다.
    private final ErrorCode errorCode;

    // ErrorCode의 기본메시지를 활용하는 예외 생성자
    // 에러코드만
    public BusinessException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
    /*
    * private: 바깥에서 함부로 건드리지 못하게함
    * final: 한 번 정해지면 아무도 바꾸지 못하게함
    * => 읽기만 가능하고 수정은 불가능 한 상태
    * */

    // 같은 오류코드에 다른 상세내용을 적고싶을때 사용하는 생성자
    // 가지고 올 변수(에러코드와 메세지) 같이
    public BusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode; // 전달 완료
    }

    // getter
    public ErrorCode getErrorCode() {
        return errorCode; //   핸들러가 에러코드를 꺼내 볼 수 있는 상태
    }
}
