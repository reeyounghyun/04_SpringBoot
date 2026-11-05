package org.example.restapi.testpractice.testsection03.exceptionenum;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "org.example.restapi.testpractice.testsection03.exceptionenum")
public class ProductExceptionHandler {

    /*
     * [문제 6-2] ProductException을 처리하는 메서드를 만드세요.
     *  - 반환 타입: ResponseEntity<ProductErrorResponse>
     *  - 상태코드 칸: 예외의 ErrorCode에서 꺼낸 HttpStatus
     *  - 본문 칸   : ProductErrorResponse.from(예외)
     * */

}
