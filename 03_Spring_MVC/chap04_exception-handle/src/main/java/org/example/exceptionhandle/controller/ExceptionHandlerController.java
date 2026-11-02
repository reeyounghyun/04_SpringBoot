package org.example.exceptionhandle.controller;

import org.example.exceptionhandle.exception.MemberRegistException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExceptionHandlerController {

    @GetMapping(value = "/controller-null")
    public String nullPointerException() {

        String str = null;
        System.out.println(str.charAt(0)); // NullPointerException 발생!!

        return "/";
    }

    @GetMapping("/controller-user")
    public String userException() throws MemberRegistException {
        boolean check = true;
        if (check) {
            throw new MemberRegistException("당신은 우리서비스의 회원이 될 수 없습니다!");
        }

        return "/";
    }



  /*
    // Exception.class로 지정하면 모든 예외를 처리할 수 있다.
    // 단, 더 구체적인 예외 핸들러(NullPointerException 등)가 있으면 그쪽이 우선 실행된다.
  @ExceptionHandler(Exception.class)
    public String nullPointerExceptionHandler(Exception exception) {
        System.out.println("controller 레벨의 exception 처리!!");
        return "error/nullPointer";
    }*/


    /*
     예외 처리
     * @ExceptionHandler
     * - 스프링 부트에서 컨트롤러 요청 처리중 발생한 예외를 처리하는 메서드(Handler)에 사용하는 어노테이션
     * - 지정한 예외가 발생하면 해당 예외처리메소드(Handler)가 실행된다.
     * */
    @ExceptionHandler(NullPointerException.class)
    public String nullPointerExceptionHandler(NullPointerException exception) {
        System.out.println("controller 레벨의 exception 처리!!");
        return "error/nullPointer";
    }

    // 사용자가 만든 예외처리
    @ExceptionHandler(MemberRegistException.class)
    public String memberRegistException(Model model, MemberRegistException exception) {
        System.out.println("controller 레벨의 exception 처리!!");
        model.addAttribute("exception", exception);

        return "error/memberRegist";
    }
}
