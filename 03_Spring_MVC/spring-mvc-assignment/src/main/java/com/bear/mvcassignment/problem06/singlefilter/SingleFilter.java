package com.bear.mvcassignment.problem06.singlefilter;

/**
 * 문제 6-1. 필터 1개 만들기 (수업: StopWatchFilter)
 *
 * 1. jakarta.servlet.Filter 인터페이스를 구현한다. @Component는 붙이지 않는다. (SingleFilterConfig에서 등록)
 * 2. init(): 서버가 켜질 때 한 번 호출된다. "[SingleFilter] init" 로그를 출력한다.
 * 3. doFilter():
 *    - ServletRequest를 HttpServletRequest로 형변환하고 getRequestURI()를 로그로 출력한다.
 *    - 전처리: "[SingleFilter] 전처리" 로그를 출력하고 System.currentTimeMillis()로 시작 시각을 저장한다.
 *    - chain.doFilter(request, response)로 다음 단계(DispatcherServlet → Controller)에 요청을 넘긴다.
 *    - 후처리: "[SingleFilter] 후처리" 로그와 전체 수행 시간(ms)을 출력한다.
 * 4. destroy(): 서버가 꺼질 때 한 번 호출된다. "[SingleFilter] destroy" 로그를 출력한다.
 *
 * 확인: 서버 시작 시 init 로그가 한 번 찍힌다.
 *       GET /filters/single 요청 시 전처리 → 컨트롤러 → 후처리(수행 시간) 순서로 로그가 찍힌다.
 *       서버 종료 시 destroy 로그가 찍힌다.
 */
public class SingleFilter {

}
