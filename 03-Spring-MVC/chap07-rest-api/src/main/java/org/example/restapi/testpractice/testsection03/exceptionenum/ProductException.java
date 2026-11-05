package org.example.restapi.testpractice.testsection03.exceptionenum;

/*
 * [문제 5] 상품 비즈니스 규칙 위반을 표현하는 사용자 정의 예외를 만드세요.
 *  - RuntimeException 상속
 *  - ProductErrorCode 필드 (읽기만 가능하게: 어떤 키워드 2개가 필요할까요?)
 *  - 생성자 1: ProductException(ProductErrorCode errorCode)
 *  - 생성자 2: ProductException(String message, ProductErrorCode errorCode)
 *             → 상세 메시지는 부모(RuntimeException)에게 전달
 *  - getErrorCode()
 * */
public class ProductException {

}
