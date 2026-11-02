package org.example.filterandinterceptor.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class StopWatchFilter implements Filter {
// 요청 앞뒤에서 시간을 재서 콘솔에 출력 // 무엇을 할지 (시간 재기)

    // 서버가 켜질 때(필터가 생성될 때) 딱 한 번 실행
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //Filter.super.init(filterConfig);

        System.out.println("[1.Filter] StopWatchFilter init - 어플리케이션 시작 시 한 번 호출 ");
    }

    // 요청이 들어올 때마다 실행
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        /*chain.doFilter()를 호출하기 전은 Filter의 전처리 영역*/
        /*
         * ServletRequest와 ServletResponse : HTTP 전용 타입이 아님
         * -> URI, HEADER, 상태코드 (HTTP)을 사용하기 위해 변환
         * */
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        System.out.println("httpRequest.getRequestURI() : " + httpRequest.getRequestURI());
        if (httpRequest.getRequestURI().equals("/filter/block")
                && httpRequest.getHeader("X-Test") == null) {

            // System.out.println("/filter/block 요청이 들어옴??");
            System.out.println("[Filter] X-Test 헤더가 없어 요청 차단.");

            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "X-Test 헤더가 필요합니다.");
            return; // Chain.doFilter()를 호출하지 않으므로 이후 과정인 DispatcherServlet. Controller 동이 실행되지 않음
        }

        /* ===== 전처리 : 컨트롤러 실행 "전" =====*/
        System.out.println("[2.Filter] 전처리 시간 측정");
        long startTime = System.currentTimeMillis(); // 시작 시각 기록

        // 다음 필터 또는 DispatcherServlet으로 요청 전달
        // -> 컨트롤러가 실행되고, 끝날 때까지 여기서 기다림
        chain.doFilter(request, response);

        System.out.println("[7.Filter] 후처리");
        // ===== 후처리 : 컨트롤러 실행 "후" =====
        long endTime = System.currentTimeMillis(); // 로직 종료 후 시간 기록

        System.out.println("[7-1.Filter] - 전체 체인 수행 시간 : " + (endTime - startTime) + "ms");

    }

    // 서버가 꺼질 때(필터가 제거될 때) 딱 한 번 실행
    @Override
    public void destroy() {
        //Filter.super.destroy();
        System.out.println("[99. Filter] destroy - 어플리케이션 종료 시 한 번 호출 ");
    }
}
