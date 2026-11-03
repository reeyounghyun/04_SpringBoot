package org.example.requestmapping.chap01;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RequestMappingApplication {

    // 핸들러 메소드 지정
    // @RequestMapping에 URL만 작성할경우
    // 모든 HTTP 메소드(GET, POST, DELETE, PUT)이 하나의 핸들러메소드로 동작함
    @RequestMapping("/menu/regist")
    public String registMenu(Model model) {

        model.addAttribute("message", "신규 등록한 핸들러메소드 호출함");
        return "mappingResult";

    }

    // 메소드 GET요청
    // @RequestMapping(value="/menu/modify", method = RequestMethod.GET)
    @GetMapping("/menu/modify")
    public String modifyMenu(Model model) {

        model.addAttribute("message", "GET 방식과 핸들러메소드 호출함");
        return "mappingResult";

    }

    /*
    * 요청 전용 어노테이션 - 해당 url의 해당 method 에 대해서만 처리할 수 있도록 제한하는 역할
    * @GetMapping ("url")
    * @PostMapping
    * @PutMapping
    * @DeleteMapping
    * @PatchMapping
    * */
}
