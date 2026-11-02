package org.example.filterandinterceptor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StopWatchController {
// stopwatch로 들어오면 2초 기다린 뒤 result.html을 보여줌

    @GetMapping("/stopwatch")
    public String stopwatch(Model model) throws InterruptedException {
        System.out.println("[4.Controller] handlerMethod 호출");

        Thread.sleep(1000);
        System.out.println("[4.Controller] 1초 경과");

        Thread.sleep(1000);
        System.out.println("[4.Controller] 2초 경과");

        model.addAttribute("수행시간", "2초");

        return "result";
    }
}
