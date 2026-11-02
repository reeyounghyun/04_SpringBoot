package com.bear.mvcassignment.problem06.multifilter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 문제 6-7. 필터 여러 개 동작 확인용 컨트롤러
 *
 * 1. 이 클래스를 @Controller로 등록하고 클래스 공통 경로를 /filters/multi로 설정한다.
 * 2. GET /filters/multi 요청 처리 메서드
 *    - 콘솔에 "[MultiFilterController] 실행" 로그를 출력한다.
 *    - Model에 message="필터 2개를 순서대로 통과했습니다."를 추가하고 논리 뷰 이름 filters/multi를 반환한다.
 * 3. GET /filters/multi/block 요청 처리 메서드
 *    - @ResponseBody를 붙여 화면 대신 문자열 "필터 차단을 통과해 컨트롤러가 실행되었습니다."를 반환한다.
 *      (수업 FilterTestController는 @RestController였지만, 여기서는 화면 메서드와 함께 쓰므로 @ResponseBody 사용)
 */

@Controller
@RequestMapping("/filters/multi")
public class MultiFilterController {

    @GetMapping
    public String multiFilter(Model model) {
        System.out.println("[MultiFilterController] 실행");
        model.addAttribute("message", "필터 2개를 순서대로 통과했습니다.");

        return "filters/multi";
    }

    @ResponseBody
    @GetMapping("/block")
    public String block() {
        return "필터 차단을 통과해 컨트롤러가 실행되었습니다.";
    }
}
