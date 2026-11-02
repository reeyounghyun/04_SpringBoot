package org.example.filterandinterceptor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StopWatchController {

    @GetMapping("/stopwatch")
    public String stopwatch() throws InterruptedException {
        System.out.println("[Controller] handlerMethod 호출");

        Thread.sleep(1000);
        System.out.println("[Controller] 1초 경과");

        Thread.sleep(1000);
        System.out.println("[Controller] 2초 경과");

        return "result";
    }
}
