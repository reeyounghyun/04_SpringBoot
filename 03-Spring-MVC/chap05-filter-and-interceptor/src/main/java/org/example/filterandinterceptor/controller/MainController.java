package org.example.filterandinterceptor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    //MainController는  "/ 로 들어오면 main.html을 보여줌 "
    @GetMapping("/")
    public String main() {
        return "main";
    }
}
