package com.bear.mvcassignment.problem06.multifilter;

import jakarta.servlet.*;
import java.io.IOException;
import jakarta.servlet.*;          // Filter, FilterChain, ServletRequest, ServletResponse, ServletException

/**
 * 문제 6-5. 두 번째 필터 만들기
 *
 * 1. jakarta.servlet.Filter 인터페이스를 구현한다. @Component는 붙이지 않는다. (MultiFilterConfig에서 등록)
 * 2. doFilter()에서 "[SecondFilter] 전처리" → chain.doFilter() → "[SecondFilter] 후처리" 순서로 로그를 출력한다.
 */
public class SecondFilter implements Filter {

 public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
     System.out.println("[SecondFilter] 전처리");
     chain.doFilter(request, response);
     System.out.println("[SecondFilter] 후처리");
 }
}
