package demo.controller;

import demo.service.DemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 웹 요청을 처리하는 계층(Controller).
 * <p>
 * {@code @RestController}는 {@code @Controller + @ResponseBody}로,
 * 메서드의 반환값을 뷰가 아닌 HTTP 응답 본문으로 그대로 보낸다.
 */
@RestController
public class DemoController {

    // 생성자 주입으로 받은 의존성은 변경되지 않도록 final로 선언한다.
    private final DemoService demoService;

    /**
     * 생성자 주입.
     * 생성자가 하나뿐이면 {@code @Autowired}를 생략해도 스프링이 자동으로 DemoService 빈을 주입한다.
     */
    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }

    /**
     * GET /demo 요청을 처리한다.
     * 흐름: Controller → Service → Repository
     */
    @GetMapping("/demo")
    public String demo() {
        return demoService.getMessage();
    }
}
