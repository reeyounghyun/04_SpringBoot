package com.bear.mvcassignment.problem07.interceptor;

import com.bear.mvcassignment.problem07.exception.AccessDeniedByInterceptorException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 문제 7-2. 인터셉터 만들기 (수업: StopWatchInterceptor)
 * <p>
 * 1. HandlerInterceptor 인터페이스를 구현하고 @Component로 등록한다.
 * 2. preHandle(): 컨트롤러 실행 "전"
 * - "[AccessInterceptor] preHandle" 로그를 출력한다.
 * - 요청 URI가 /interceptors/exception 이고 access 파라미터가 "granted"가 아니면
 * new AccessDeniedByInterceptorException("access=granted 파라미터가 필요합니다.")를 던진다.
 * (수업에서는 sendError() 후 return false로 막았지만, 여기서는 예외를 던진다.)
 * - 통과하면 request.setAttribute("interceptorStartTime", System.currentTimeMillis())로 시작 시각을 저장하고 true를 반환한다.
 * 3. postHandle(): 컨트롤러 실행 "후", 뷰 렌더링 "전"
 * - "[AccessInterceptor] postHandle" 로그를 출력한다.
 * - 저장해 둔 시작 시각으로 수행 시간을 계산하고, modelAndView가 null이 아니면 interceptorInterval로 추가한다.
 * 4. afterCompletion(): 뷰 렌더링까지 모두 끝난 뒤
 * - "[AccessInterceptor] afterCompletion" 로그와 함께 매개변수 ex 값도 출력한다.
 * <p>
 * 확인: preHandle에서 예외를 던졌을 때 postHandle, afterCompletion은 각각 실행되는가?
 */
@Component
public class AccessInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        System.out.println("[AccessInterceptor] preHandle 컨트롤러 실행 전 입니다.");
        // 요청 파라미터를 검사해 Interceptor에서 요청을 차단
        if (request.getRequestURI().equals("/interceptors/exception")
                && !"granted".equals(request.getParameter("access"))) {
            System.out.println("[AccessInterceptor] access 값이 없어 예외 발생");

            throw new AccessDeniedByInterceptorException("access=granted 파라미터가 필요합니다.");
        }
        request.setAttribute("interceptorStartTime", System.currentTimeMillis());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, ModelAndView modelAndView) throws Exception {
        long beForeCompletion = (long) request.getAttribute("interceptorStartTime");
        long interval = (long) System.currentTimeMillis() - beForeCompletion;

        System.out.println("[AccessInterceptor] postHandle 컨트롤러 실행 후");
        if (modelAndView != null) {
            modelAndView.addObject("interceptorInterval", interval);
        }
        System.out.println("[AccessInterceptor] postHandle 뷰 렌더링 전");

    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) throws Exception {
        System.out.println("[AccessInterceptor] afterCompletion, ex = " + ex);
    }
}
