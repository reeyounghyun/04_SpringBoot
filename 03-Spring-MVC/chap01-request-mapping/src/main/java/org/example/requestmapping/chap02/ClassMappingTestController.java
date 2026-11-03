package org.example.requestmapping.chap02;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


// /** : /order 아래의 모든 경로와 모든 HTTP 메소드 요청을 처리한다.
// 더 구체적으로 매핑된 URL(/regist, /detail/{orderNo} 등)이 우선 처리된다.
@Controller
@RequestMapping("/order")
//@RequestMapping("/order/*")
public class ClassMappingTestController {

    @GetMapping("/regist")
    public String registOrder(Model model) {

        model.addAttribute("message", "/order/regist 요청함");
        return "mappingResult";
    }

    // 여러개의 요청 매핑 : @RequestMapping value 속성에 중괄호를 이용해 매팅할 URL을 나열 가능!
    // POST / order/modify
    // POST / order/delete
    @RequestMapping(value = { "modify", "delete"}, method = {RequestMethod.POST})
    public String modifyAndDelete(Model model) {

        model.addAttribute("message", " order/modify  와 / order/delete , 요청결과");
        return "mappingResult";
    }

    // PathVariable : URL에서 변수를 받아올 수 있다.
    // @PathVariable(Path에 적힉 변수명) 타입 변수명
    // @PathVariable 타입 Path에 적힌 변수명
    @GetMapping("/detail/{orderNo}")
    public String selectOrderDetail(Model model, @PathVariable("orderNo") int orderNo) {

        model.addAttribute("message", orderNo + "번 주문 상세조회합니다.");
        return "mappingResult";
    }

}

