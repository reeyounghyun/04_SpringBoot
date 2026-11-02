package org.example.filterandinterceptor.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Interceptor 요청 차단
@RestController // 화면이 아닌 문자를 리턴
@RequestMapping("/interceptor")
public class InterceptorTestController {

    @GetMapping("/block")
    public String block() {

        return "Interceptor를 통과하여 Controller가 실행되었습니다.";
    }

}
