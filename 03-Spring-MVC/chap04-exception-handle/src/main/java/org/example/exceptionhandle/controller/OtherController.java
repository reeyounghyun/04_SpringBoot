package org.example.exceptionhandle.controller;

import org.example.exceptionhandle.exception.MemberRegistException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OtherController {

    @GetMapping("/other-controller-null")
    public String otherControllerException(){
        String str = null;
        System.out.println(str.charAt(0)); // Null Pointer Exception 발생

        return "/";
    }

    @GetMapping("/other-controller-user")
    public String userException() throws MemberRegistException {
        boolean check = true;
        if (check) {
            throw new MemberRegistException("당신은 우리서비스의 회원이 될 수 없습니다!");
        }

        return "/";
    }

    @GetMapping("/other-controller-array")
    public String arrayIndexOutOfBoundsException() {
        int[] arr = new int[1];
        System.out.println(arr[1]); // ArrayIndexOutOfBoundsException 발생 (0번만 있음)

        return "/";
    }
}
