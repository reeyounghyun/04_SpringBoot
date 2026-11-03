package org.example.handlermethod;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/first/*")
public class FirstController {


    // 반환형이 Void일 경우, 요청경로(Path) 경로를 논리적인 View 이름으로 사용
    @GetMapping("regist")
    public String regist() {

        return "first/regist";
    }

    @PostMapping("regist")
    public String registMenu(Model model, HttpServletRequest request) {

        // HttpServletRequest : 서블릿이 제공하는 요청 객체로, 요청 방식·URL 등 HTTP 요청 정보를 직접 꺼낼 수 있다.
        System.out.println(request.getMethod());        // 요청 방식 (GET, POST 등)
        System.out.println(request.getRequestURL());    // 요청 전체 URL (http://localhost:8080/first/regist)

        System.out.println(request.getHeaderNames());

        String name = request.getParameter("name");
        System.out.println("name = " + name);
        int price = Integer.parseInt(request.getParameter("price"));
        System.out.println("price = " + price);
        int categoryCode = Integer.parseInt(request.getParameter("categoryCode"));
        System.out.println("categoryCode = " + categoryCode);

      /*  String message = name + "을(를) 신규 메뉴 목록의 " + categoryCode + "번 카테고리에 " + price + "원으로 등록 하셨습니다!";
        model.addAttribute("message", message);*/

        model.addAttribute("message", name + "을 신규메뉴목록의 " + categoryCode + " 번, 가격은 " + price + "원으로 추가했습니다.");

        return "first/messagePrinter";
    }

    // view 이동용
    @GetMapping("modify")
    public void modify() {

    }

    // @RequestParam : 요청 파라미터를 매개변수에 바로 바인딩한다. (매개변수 이름 = 폼 input의 name)
    // - 생략 가능하다.

    // required: 기본값은 true, 필수 파라미터가 없으면 400 예외 발생
    // defaultValue: 값이 없거나 비어 있을 때 사용할 기본값 지정
    @PostMapping("modify")
    public String modifyMenu(
            Model model,
            @RequestParam String modifyName,
            @RequestParam String modifyPrice
    ) {
        String message = modifyName + "을 " + modifyPrice + "원으로 수정했습니다.";
        model.addAttribute("message", message);

        return "first/messagePrinter";
    }


    @GetMapping("search")
    public void search() {

    }


    /*
     * @ModelAttribute: 여러 요청 파라미터를 DTO 같은 객체에 바인딩
     * -- 핸들러 메소드의 매개변수로 전달되는 동시에 Model에도 자동으로 추가된다.
     * -- Model에 담길 변수는 이름을 작성하지 않으면 클래스명의 소문자 카멜케이스로 지정된다.
     * -- Model에 담기는 이름은 매개변수 변수명(menu)과 상관없다.
     *    이름을 지정하지 않으면 항상 클래스 이름(MenuDTO)에서 첫 글자만 소문자로 바꾼 menuDTO가 된다.
     *      @ModelAttribute MenuDTO menu          → Model에 "menuDTO"로 담김
     *      @ModelAttribute("menu") MenuDTO menu  → Model에 "menu"로 담김
     * -- 이름을 지정하지 않는 방법도 있다.
     *    컨트롤러는 그대로 두고 HTML 쪽을 ${ menuDTO.name }으로 바꿔도 동작한다. (둘 중 하나로 이름만 맞추면 된다.)
     * */
    @PostMapping("search")
    public String searchMenu(@ModelAttribute("menu") MenuDTO menu) {
        return "first/searchResult";
    }


    @GetMapping("login")
    public void login() {

    }

    /*
     * session: 사용자별 정보를 일정시간 동안 서버에 보관하는 저장소
     * -- 세션은 사물함, JSESSIONID는 열쇠 번호
     *    정보(로그인 id 등)는 서버의 사물함에 있고, 브라우저는 열쇠 번호(쿠키)만 들고 다닌다.
     *
     * Session : 사용자별 정보를 일정시간 동안 보관하는 공간 (서버측)
     * - key-value 쌍으로 보관
     * - 로그인상태, 장바구니같은 임시정보를 유지할때 사용
     *
     * -- [세션] 동작과정 (로그인할 때의 흐름)
     *    1. 사용자가 처음, 서버에 요청
     *    2. 서버가 사용자 전용 세션(사물함)을 만듦 (JSESSIONID : 식별키)
     *    3. 서버가 사용자에게 응답할 때 JSESSIONID 쿠키 전달
     *    4. 브라우저가 이후 요청마다 JSESSIONID 쿠키를 전송
     *    5. 서버: 쿠키 번호를 보고 그 사용자의 세션을 찾아서 꺼냄
     *
     * -- 서버의 세션 저장소 예시
     *    JSESSIONID        보관된 값
     *    a12345      →     id = "bear"   (기본 크롬)
     *    b12345      →     id = "tiger"  (시크릿탭 크롬)
     *
     * -- 같은 크롬의 탭끼리는 쿠키를 공유하므로 세션도 하나다.
     *    시크릿탭은 쿠키 저장소가 따로라서 다른 사용자처럼 새 세션이 만들어진다.
     * -- 쿠키(JSESSIONID)를 지우면 서버의 세션을 찾아갈 수 없어서 새 세션이 만들어진다. (로그인 풀림)
     * -- 확인: 개발자도구 → Application → Cookies → JSESSIONID
     * */
    @PostMapping("login")
    public String loginSession(String id, String pwd, HttpSession session) {

        // 값을 담아주기
        session.setAttribute("id", id); // 세션에 값 저장

        return "first/loginResult";
    }

    /*
     * -- [로그아웃] 동작과정
     *    1. 브라우저가 /first/logout 요청 (JSESSIONID 쿠키와 함께)
     *    2. 서버: 쿠키 번호로 그 사용자의 세션을 찾음
     *    3. session.invalidate() → 세션(사물함)과 안에 담긴 값(id)을 모두 삭제
     *    4. 이후 ${session.id}는 null이 되어 로그인 안 된 상태로 보임
     *       (다음 요청 때 필요하면 새 세션이 새 번호로 만들어짐)
     * */
    @GetMapping("logout")
    public String logoutTest(HttpSession session) {
        session.invalidate(); // 세션 삭제
        return "first/loginResult";
    }

    @GetMapping("body")
    public void body() {

    }

    /*
     * @RequestBody : HTTP 요청 본문을 읽어서 핸들러 메소드의 매개변수로 전달
     * 일반 form 요청은 쿼리문자열의 형태이다. => ex) body : name=마라탕&price=10000&categoryCode=1&orderableStatus=y
     *  JSON은 JACKSON 메시지 컨버터로 객체로 변환도 가능
     *
     * -- 요청에서 값을 꺼내는 어노테이션
     *    어노테이션        꺼내는 곳
     *    @RequestBody      요청 본문
     *    @RequestHeader    요청 헤더
     *    @CookieValue      쿠키
     * */
    @PostMapping("body")
    public void bodyTest(
            @RequestBody String body,
            @RequestHeader("content-type") String contentType,
            @CookieValue(value = "JSESSIONID", required = false) String sessionId
    ) throws UnsupportedEncodingException {

        System.out.println("body : " + URLDecoder.decode(body, StandardCharsets.UTF_8));
        System.out.println("contentType = " + contentType);
        System.out.println("sessionId = " + sessionId);
    }
    /* 결과
    *   body : name=피자&price=1000&categoryCode=1&orderableStatus=y
        contentType = application/x-www-form-urlencoded
        sessionId = 400DA1739AD6C747B5B431A7E61F27E5*/
}
