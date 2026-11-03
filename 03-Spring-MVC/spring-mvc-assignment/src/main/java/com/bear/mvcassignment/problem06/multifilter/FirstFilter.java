package com.bear.mvcassignment.problem06.multifilter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 문제 6-4. 첫 번째 필터 만들기 + 요청 차단 (수업: StopWatchFilter의 X-Test 차단)
 * <p>
 * 1. jakarta.servlet.Filter 인터페이스를 구현한다. @Component는 붙이지 않는다. (MultiFilterConfig에서 등록)
 * 2. doFilter():
 * - request, response를 HttpServletRequest, HttpServletResponse로 형변환한다.
 * - 요청 URI가 /filters/multi/block 이고 X-Test 헤더가 없으면
 * "[FirstFilter] X-Test 헤더가 없어 요청 차단" 로그를 출력하고
 * sendError(HttpServletResponse.SC_FORBIDDEN, "X-Test 헤더가 필요합니다.")를 호출한 뒤 return 한다.
 * (chain.doFilter()를 호출하지 않으므로 SecondFilter와 컨트롤러가 실행되지 않는다.)
 * - 그 외에는 "[FirstFilter] 전처리" → chain.doFilter() → "[FirstFilter] 후처리" 순서로 로그를 출력한다.
 */
public class FirstFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        System.out.println("httpRequest.getRequestURI() : " + httpRequest.getRequestURI());
        if (httpRequest.getRequestURI().equals("/filters/multi/block")
                && httpRequest.getHeader("X-Test") == null) {

            return;
        }
        System.out.println("[SecondFilter] X-Test 헤더가 없어 요청 차단");
        httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "X-Test 헤더가 필요합니다.");

        System.out.println("[FirstFilter] 전처리");
        chain.doFilter(request, response);
        System.out.println("[FirstFilter] 후처리");

    }
}
