package com.bear.studyboard.monitoring.section05.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

// 요청마다 요청식별자를 만들어 로그에 붙이는 필터
@Component
public class TraceIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. 요청 ID 만들기
        // : 요청마다 겹치지 않는 번호를 만듭니다. UUID 전체(36자)는 너무 길어서 앞 8자리만 씁니다.
        String traceId = UUID.randomUUID().toString().substring(0, 8);

        // 2. MDC에 담기
        // : MDC는 현재 스레드 전용 저장공간입니다. 여기에 넣어 두면 컨트롤러, 서비스, 예외처리 등 어디서 log.info()를 찍어도 번호를 따로 넘기지 않아도 됩니다. 로그 패턴의 %X{traceId}가 꺼내서 자동으로 붙입니다.
        MDC.put("traceId", traceId);

        // 3. 사용자에게 traceID 제공 -> 사용자가 오류를 겪었을때 로그를 찾을 수 있는 수단이 된다.
        // : 응답 헤더로 사용자에게도 번호를 알려 줍니다. 사용자가 "에러가 났어요, 번호는 a1b2c3d4예요"라고 알려 주면 개발자가 그 번호로 로그를 바로 찾을 수 있습니다.
        response.setHeader("X-Trace-Id", traceId);

        try {
            // : 다음 필터와 컨트롤러로 요청을 넘깁니다. 이 줄이 없으면 요청이 필터에서 멈춥니다.
            filterChain.doFilter(request, response);
        } finally {
            // 서버에서는 스레드를 재사용하므로 지우지 않으면 앞요청의 번호가 남아있있어, 뒤셖이는 문제가 발생함
            // : 서버는 스레드를 재사용합니다(스레드 풀). 번호를 지우지 않으면 다음 요청이 그 스레드를 받았을 때 이전 요청의 번호가 남아 로그가 엉킵니다. finally에 두어서 예외가 나도 반드시 지워지게 합니다.
            MDC.remove("traceId");
        }

    }
}
