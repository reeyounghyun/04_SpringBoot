package com.bear.mvcassignment.problem06.multifilter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 문제 6-6. 필터 여러 개 등록하고 순서 정하기 (과제: Filter를 여러 개 붙이고 싶다)
 * <p>
 * 1. 이 클래스를 @Configuration으로 등록한다.
 * 2. FirstFilter와 SecondFilter를 각각 FilterRegistrationBean으로 등록하는 @Bean 메서드를 2개 만든다.
 * - 두 필터 모두 addUrlPatterns()로 /filters/multi, /filters/multi/* 경로에 적용한다.
 * - setOrder()로 FirstFilter는 1, SecondFilter는 2를 지정한다. (숫자가 작을수록 먼저 실행)
 * - setName()으로 각각 "firstFilter", "secondFilter" 이름을 붙인다.
 * <p>
 * 확인:
 * - GET /filters/multi 요청 시 콘솔 로그 순서
 * First 전처리 → Second 전처리 → 컨트롤러 → Second 후처리 → First 후처리
 * - order 값을 서로 바꿔서 실행 순서가 바뀌는지 확인한다.
 * - 브라우저로 GET /filters/multi/block 요청 → 403 에러, SecondFilter와 컨트롤러 로그가 찍히지 않는다.
 * - Postman에서 X-Test 헤더를 넣고 요청 → 두 필터를 통과해 컨트롤러 메시지가 응답된다.
 */
@Configuration
public class MultiFilterConfig {

    @Bean
    public FilterRegistrationBean<FirstFilter> firstFilter() {
        FilterRegistrationBean<FirstFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new FirstFilter());

        registration.addUrlPatterns("/filters/multi", "/filters/multi/*");

        registration.setOrder(1);
        registration.setName("firstFilter");

        return registration;
    }

    @Bean
    public FilterRegistrationBean<SecondFilter> secondFilter() {
        FilterRegistrationBean<SecondFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new SecondFilter());

        registration.addUrlPatterns("/filters/multi", "/filters/multi/*");

        registration.setOrder(2);   // FirstFilter(1) 다음에 실행
        registration.setName("secondFilter");

        return registration;
    }
}


