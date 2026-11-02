package com.bear.mvcassignment.problem06.singlefilter;

/**
 * 문제 6-2. 필터 1개 등록하기 (수업: FilterConfiguration)
 *
 * 1. 이 클래스를 @Configuration으로 등록한다.
 * 2. FilterRegistrationBean<SingleFilter>을 반환하는 @Bean 메서드를 만든다.
 *    - setFilter()로 new SingleFilter()를 넣는다.
 *    - addUrlPatterns()로 /filters/single 경로에만 적용한다.
 *    - setOrder(1)로 순서를 지정한다.
 *    - setName("singleFilter")로 필터 이름을 붙인다.
 *
 * 확인: /filters/single 요청에서만 SingleFilter 로그가 찍히고, / (홈) 요청에서는 찍히지 않는다.
 */
public class SingleFilterConfig {

}
