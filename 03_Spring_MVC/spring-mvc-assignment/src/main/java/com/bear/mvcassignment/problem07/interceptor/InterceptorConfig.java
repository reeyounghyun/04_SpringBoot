package com.bear.mvcassignment.problem07.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 문제 7-3. 인터셉터 등록하기 (수업: WebConfiguration)
 * <p>
 * 1. 이 클래스를 @Configuration으로 등록하고 WebMvcConfigurer를 구현한다.
 * 2. AccessInterceptor를 생성자 주입으로 받는다.
 * 3. addInterceptors(InterceptorRegistry registry)를 오버라이드한다.
 * - addInterceptor()로 AccessInterceptor를 추가한다.
 * - addPathPatterns()로 /interceptors, /interceptors/** 경로에 적용한다.
 * - excludePathPatterns()로 /error 경로는 제외한다.
 * <p>
 * 확인: /interceptors 요청에서만 인터셉터 로그가 찍히고, /filters/single 요청에서는 찍히지 않는다.
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private AccessInterceptor accessInterceptor;

    public InterceptorConfig(AccessInterceptor accessInterceptor) {
        this.accessInterceptor = accessInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessInterceptor)
                .addPathPatterns("/interceptors", "/interceptors/**")
                .excludePathPatterns("/css/**", "/images/**", "/js/**", "/error");
    }

}
