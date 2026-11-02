package org.example.exceptionhandle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OtherController {

    @GetMapping("other-controller-null")
    public String otherControllerException(){
        String str = null;
        System.out.println(str.charAt(0)); // Null Pointer Exception 발생

        return "/";
    }
}
