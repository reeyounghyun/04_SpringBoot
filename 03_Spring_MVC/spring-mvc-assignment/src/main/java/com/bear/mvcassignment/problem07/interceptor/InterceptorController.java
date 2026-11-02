package com.bear.mvcassignment.problem07.interceptor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 문제 7-4. 인터셉터 동작 확인용 컨트롤러
 * <p>
 * 1. 이 클래스를 @Controller로 등록하고 클래스 공통 경로를 /interceptors로 설정한다.
 * 2. GET /interceptors 요청 처리 메서드
 * - 콘솔에 "[InterceptorController] 실행" 로그를 출력한다.
 * - Model에 message="인터셉터를 통과했습니다."를 추가하고 논리 뷰 이름 interceptors/result를 반환한다.
 * 3. GET /interceptors/exception 요청 처리 메서드
 * - Model에 message="access=granted로 인터셉터를 통과했습니다."를 추가하고 interceptors/result를 반환한다.
 * <p>
 * 확인:
 * - /interceptors/exception → 인터셉터에서 예외 발생, 이 컨트롤러 메서드는 실행되지 않는다.
 * - /interceptors/exception?access=granted → 정상 통과, 화면에 메시지와 수행 시간이 표시된다.
 */

@Controller
@RequestMapping("/interceptors")
public class InterceptorController {

    @GetMapping
    public String interceptors(Model model) {
        System.out.println("[InterceptorController] 실행");
        model.addAttribute("message", "인터셉터를 통과했습니다.");
        return "interceptors/result";
    }

    @GetMapping("/exception")
    public String exception(Model model) {
        System.out.println("[InterceptorController] 실행");
        model.addAttribute("message", "\"[InterceptorController] exception 실행 access=granted로 인터셉터를 통과했습니다.");
        return "interceptors/result";
    }
}
