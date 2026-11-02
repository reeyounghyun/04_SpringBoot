package org.example.filterandinterceptor.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/*
 * Interceptor
 * - DispatcherServlet 내부에서 선택된 Handler(Controller)를 기준으로 동작
 * - Controller에 가까움(스프링컨텍스트 내부) Handler 정보와 ModelAndView를 사용 가능
 * */
@Component
public class StopWatchInterceptor implements HandlerInterceptor {

    /*
     * preHandle
     * Controller(handlerMethod) 실행 "전"에 호출
     * 반환값이 true : 다음 단계(Controller)로 진행, false : 요청 중단 (Controller 실행 안 됨)
     * */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("[Interceptor] preHandle - 컨트롤러 호출 전 !");

        // 요청 파라미터를 검사해 Interceptor에서 요청을 차단
        if (request.getRequestURI().equals("/interceptor/block")
                && !"granted".equals(request.getParameter("access"))) {
            System.out.println("[Interceptor] access 값이 없어 - 접근 거부");

            response.sendError(HttpServletResponse.SC_FORBIDDEN, "access=granted 파라미터가 필요합니다.");
            return false;
        }

        request.setAttribute("interceptorStartTime", System.currentTimeMillis());

        return true;

        //return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    /*
     * postHandle()
     * - Controller 호출 이후 실행, View가 렌더링 되기 전에 실행
     * - ModelAndView가 존재하면 전달할 데이터를 추가하거나 수정 가능
     * */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        long startTime = (long) request.getAttribute("interceptorStartTime");
        long interval = System.currentTimeMillis() - startTime;

        System.out.println("[Interceptor] postHandle - 컨트롤러 호출 후, 뷰 랜더링 전 !");
        // @RestController 처럼 화면이 없는 응답은 modelAndView가 null
        if (modelAndView != null) {
            modelAndView.addObject("interceptorInterval", interval);
        }

        //HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /*
     * afterCompletion()
     * - View 렌더링을 포함한 요청 처리가 모두 끝난 뒤 실행
     * - 예외에 대한 정보도 받을 수 있음
     * */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        //HandlerInterceptor.super.afterCompletion(request, response, handler, ex);

        System.out.println("[Interceptor] afterCompletion - 뷰 랜더링 후 !");
    }
}
