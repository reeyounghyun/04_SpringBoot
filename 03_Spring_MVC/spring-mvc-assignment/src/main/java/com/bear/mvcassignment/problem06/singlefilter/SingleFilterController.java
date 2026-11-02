package com.bear.mvcassignment.problem06.singlefilter;

/**
 * 문제 6-3. 필터 1개 동작 확인용 컨트롤러
 *
 * 1. 이 클래스를 @Controller로 등록한다.
 * 2. GET /filters/single 요청을 처리하는 메서드를 만든다.
 *    - 콘솔에 "[SingleFilterController] 실행" 로그를 출력한다.
 *    - Model에 message="필터 1개를 통과했습니다."를 추가한다.
 *    - 논리 뷰 이름 filters/single을 반환한다.
 *
 * 확인: 화면에 메시지가 표시되고, 콘솔 로그가 필터 전처리 → 컨트롤러 → 필터 후처리 순서로 찍힌다.
 */
public class SingleFilterController {

}
