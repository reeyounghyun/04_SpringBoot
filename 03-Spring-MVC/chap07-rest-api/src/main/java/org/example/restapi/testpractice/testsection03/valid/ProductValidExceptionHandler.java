package org.example.restapi.testpractice.testsection03.valid;

import org.springframework.web.bind.annotation.RestControllerAdvice;

/*
 * basePackages: 이 패키지(testsection03.valid)의 컨트롤러에서 난 예외만 처리해요.
 * */
@RestControllerAdvice(basePackages = "org.example.restapi.testpractice.testsection03.valid")
public class ProductValidExceptionHandler {

    /*
     * [문제 3] 검증 실패 예외를 처리하는 메서드를 만드세요.
     *  - 어떤 예외 클래스를 잡아야 할까요? (원본 ValidExceptionHandler 참고)
     *  - 상태코드 400
     *  - 응답: { "field": "실패한 필드명", "message": "어노테이션에 적은 메시지" }
     *
     * [보너스 1] getFieldErrors()로 실패한 필드를 전부 담아 응답해 보세요.
     * */

}
