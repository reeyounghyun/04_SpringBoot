package org.example.filterandinterceptor.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

// ① "이 클래스는 설정 파일이야 // 어디서 할지 (/stopwatch, /filter/* 주소에서만)"
public class FilterConfiguration {
//  "StopWatchFilter를 /stopwatch 주소에 붙여라"라고 Spring에 등록

   /*
   * Filter는 servlet 기술이므로 servlet Container에 등록해야함.
   * FilterRegistrationBean
   * - 어떤 url에 적용될지
    * - spring Context에 Bean으로 FILTER를 등록 할 수 있게 해주는 클래스
    * */


    // ② 이 메서드가 돌려주는 걸 Spring이 가져가서 관리
    @Bean
    public FilterRegistrationBean<StopWatchFilter> stopWatchFilter() {
        FilterRegistrationBean<StopWatchFilter> registration = new FilterRegistrationBean<>();    // ③ 필터 등록 신청서 한 장 만들기
        registration.setFilter(new StopWatchFilter());   // ④ 신청서에 "등록할 필터 = StopWatchFilter"

        // 해당 패턴의 URL로 요청이 왔을때, 이 필터가 동작하겠다.
        registration.addUrlPatterns("/stopwatch", "/filter/*");

        registration.setOrder(1); // 필터의 적용 순서를 명시 (숫자가 작을수록 먼저 실행됨)  ⑥ 필터가 여러 개면 1번째로 실행
        registration.setName("stopWatchFilter");  // ⑦ 필터 이름 붙이기

        return registration; // ⑧ 다 쓴 신청서를 Spring에 제출
    }
}
