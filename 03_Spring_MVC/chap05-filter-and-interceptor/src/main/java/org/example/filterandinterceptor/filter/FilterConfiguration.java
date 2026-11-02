package org.example.filterandinterceptor.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {

    // Filter는 servlet 기술이므로 servlet Container에 등록해야함.
    // FilterRegistrationBean
    // -어떤 url에 적용될지
    // - spring Context에 Bean으로 FILTER를 등록 할 수 있게 해주는 클래스


    @Bean
    public FilterRegistrationBean<StopWatchFilter> stopWatchFilter() {
        FilterRegistrationBean<StopWatchFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new StopWatchFilter());

        // 해당 패턴의 URL로 요청이 왔을때, 이 필터가 동작하겠다.
        registration.addUrlPatterns("/stopwatch", "/filter/*");

        registration.setOrder(1); // 필터의 적용 순서를 명시 (숫자가 작을수록 먼저 실행됨)
        registration.setName("stopWatchFilter");

        return registration;
    }
}
