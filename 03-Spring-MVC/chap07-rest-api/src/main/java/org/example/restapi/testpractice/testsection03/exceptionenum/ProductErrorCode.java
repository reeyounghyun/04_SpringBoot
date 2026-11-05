package org.example.restapi.testpractice.testsection03.exceptionenum;

import org.springframework.http.HttpStatus;

// 상품 관련 예외 정보를 관리하는 Enum
public enum ProductErrorCode {

    // 예시
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT_NOT_FOUND", "상품을 찾을 수 없습니다.");

    /*
     * [문제 4] 아래 상수 3개를 추가하세요.
     *  - INVALID_PRODUCT_NO : 400, "상품 번호는 1 이상이어야 합니다."
     *  - INVALID_QUANTITY   : 400, "주문 수량은 1개 이상이어야 합니다."
     *  - OUT_OF_STOCK       : 409, "재고가 부족합니다."
     *
     * 주의: 상수 사이는 쉼표(,), 마지막 상수 뒤에만 세미콜론(;)
     * */

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ProductErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
