package org.example.filterandinterceptor.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*-
 implements WebMvcConfigurer: "Spring MVC 설정을 바꾸겠다"는 표시
- addInterceptors(): Spring이 서버를 켤 때 이 메서드를 불러서 인터셉터 목록을 받아 감
- addPathPatterns(): 필터의 addUrlPatterns()처럼 적용할 주소를 정함
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private StopWatchInterceptor stopWatchInterceptor;

    // 생성자 주임
    public WebConfiguration(StopWatchInterceptor stopWatchInterceptor) {
        this.stopWatchInterceptor = stopWatchInterceptor;
    }

    // 인터셉터를 추가하는 메소드
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // InterceptorRegistry :String MVC에서 관리하는 등록객체
        registry.addInterceptor(stopWatchInterceptor)
                .addPathPatterns("/stopwatch", "/interceptor/**")
                .excludePathPatterns("/css/**", "/images/**", "/js/**", "/error");
    }
}
