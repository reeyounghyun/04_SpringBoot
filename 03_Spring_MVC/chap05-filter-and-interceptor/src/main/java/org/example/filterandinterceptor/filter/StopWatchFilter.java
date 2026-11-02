package org.example.filterandinterceptor.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class StopWatchFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        long start = System.currentTimeMillis();

        // 다음 필터 또는 DispatcherServlet으로 요청 전달
        chain.doFilter(request, response);

        long end = System.currentTimeMillis();
        String uri = ((HttpServletRequest) request).getRequestURI();
        System.out.println("[StopWatchFilter] " + uri + " 처리 시간 : " + (end - start) + "ms");
    }
}
