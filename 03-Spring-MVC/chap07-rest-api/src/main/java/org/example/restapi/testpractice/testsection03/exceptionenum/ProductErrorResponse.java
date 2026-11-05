package org.example.restapi.testpractice.testsection03.exceptionenum;

/*
 * [문제 6-1] 예외 발생 시 클라이언트에 보낼 응답 DTO를 만드세요.
 *  - 필드: int status, String code, String message, String detail
 *  - 생성자 (필드 4개)
 *  - public static ProductErrorResponse from(ProductException exception)
 *      status  : 상태코드 숫자 (HttpStatus → 숫자는 .value())
 *      code    : 에러 코드
 *      message : ErrorCode에 정의한 기본 메시지
 *      detail  : 예외를 만들 때 넣은 상세 메시지
 *  - getter (없으면 JSON에 값이 안 나와요!)
 *
 * 응답 예시:
 * {
 *   "status": 404,
 *   "code": "PRODUCT_NOT_FOUND",
 *   "message": "상품을 찾을 수 없습니다.",
 *   "detail": "상품번호 99에 해당하는 상품이 없습니다."
 * }
 * */
public class ProductErrorResponse {

}
