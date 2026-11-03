# chap02-handler-method

컨트롤러의 **핸들러 메소드**가 요청 파라미터를 받는 여러 방법을 비교하는 실습.
같은 메뉴 입력 폼을 `HttpServletRequest` → `@RequestParam` → `@ModelAttribute` → `@RequestBody` 순서로 받아 보고,
`HttpSession`으로 로그인 정보를 유지·삭제한다.

- Spring Boot 4.1.2 / Java 21
- Thymeleaf, Spring Web MVC, Lombok, devtools

## 공통 흐름

```
① index.html 버튼 클릭        GET  /first/xxx   → 입력 폼 화면 (templates/first/xxx.html)
        ↓
② 폼 전송                    POST /first/xxx   → 핸들러 메소드가 파라미터를 받음
        ↓
③ Model / Session에 값 담기   model.addAttribute(...), session.setAttribute(...)
        ↓
④ 결과 화면 반환              return "first/messagePrinter" → Thymeleaf가 ${ } 자리에 값을 채움
```

> 폼 input의 `name` 속성이 서버에서 값을 꺼내는 **이름**이 된다.
> HTML의 `name`과 컨트롤러의 매개변수·필드 이름이 다르면 값이 들어오지 않는다.

## 디렉토리 구조

```
src/main
├── java/org/example/handlermethod
│   ├── Chap02HandlerMethodApplication.java
│   ├── FirstController.java        # @RequestMapping("/first/*") - 실습 핸들러 메소드 전체
│   └── MenuDTO.java                # @ModelAttribute 커맨드 객체 (Lombok)
└── resources
    ├── static
    │   └── index.html              # 실습 메뉴 (Thymeleaf를 거치지 않는 정적 파일)
    └── templates/first
        ├── regist.html             # 1. 메뉴 등록 폼
        ├── messagePrinter.html     #    결과 메시지 출력 (regist, modify 공용)
        ├── modify.html             # 2. 메뉴 수정 폼
        ├── search.html             # 3. 메뉴 검색 폼
        ├── searchResult.html       #    커맨드 객체 출력
        ├── login.html              # 4. 로그인 폼
        ├── loginResult.html        #    세션 값 확인 + 로그아웃
        └── body.html               # 5. @RequestBody 폼
```

## 뷰 이름 규칙

| 반환형 | 뷰 이름 | 예시 |
|---|---|---|
| `String` | 반환한 문자열 | `return "first/regist";` → `templates/first/regist.html` |
| `void` | **요청 경로**를 그대로 사용 | `GET /first/modify` → `templates/first/modify.html` |

`templates/` + 뷰 이름 + `.html` 위치에서 파일을 찾는다.
클래스의 `@RequestMapping("/first/*")`와 `templates/first/` 폴더 이름을 맞춰 두면 `void` 메소드만으로 화면 이동이 된다.

## 1. HttpServletRequest - 직접 꺼내기

```java
@PostMapping("regist")
public String registMenu(Model model, HttpServletRequest request) {
    System.out.println(request.getMethod());       // POST
    System.out.println(request.getRequestURL());   // http://localhost:8080/first/regist

    String name = request.getParameter("name");
    int price = Integer.parseInt(request.getParameter("price"));   // 문자열 → 숫자 직접 변환

    model.addAttribute("message", name + "을 ... 추가했습니다.");
    return "first/messagePrinter";
}
```

- 서블릿 요청 객체에서 `getParameter()`로 하나씩 꺼낸다. 값은 항상 **문자열**이다.
- 요청 방식, URL, 헤더 같은 HTTP 요청 정보를 직접 볼 수 있다.

## 2. @RequestParam - 파라미터 하나씩 바인딩

```java
@PostMapping("modify")
public String modifyMenu(Model model,
                         @RequestParam String modifyName,
                         @RequestParam String modifyPrice) { ... }
```

- 매개변수 이름 = 폼 input의 `name`
- `required` : 기본값 `true`, 필수 파라미터가 없으면 **400** 오류
- `defaultValue` : 값이 없거나 비어 있을 때 사용할 기본값

## 3. @ModelAttribute - 객체로 한 번에 바인딩

```java
@PostMapping("search")
public String searchMenu(@ModelAttribute("menu") MenuDTO menu) {
    return "first/searchResult";   // ${ menu.name } 으로 출력
}
```

- 여러 파라미터를 DTO(커맨드 객체)에 담는다. 필드 이름 = 폼 input의 `name`
- 기본 생성자로 객체를 만들고 **setter**로 값을 넣으므로 setter가 필요하다. (`@Setter`)
- 매개변수로 받는 동시에 **Model에도 자동으로 추가**된다.

### Model에 담기는 이름

| 선언 | Model 속성 이름 |
|---|---|
| `@ModelAttribute MenuDTO menu` | `menuDTO` (클래스명의 첫 글자만 소문자) |
| `@ModelAttribute("menu") MenuDTO menu` | `menu` |

변수 이름(`menu`)과는 상관없다. 이름을 지정하거나, HTML 쪽을 `${ menuDTO.name }`으로 맞추면 된다.

## 4. HttpSession - 로그인 / 로그아웃

```java
@PostMapping("login")
public String loginSession(String id, String pwd, HttpSession session) {
    session.setAttribute("id", id);    // 세션에 값 저장
    return "first/loginResult";
}

@GetMapping("logout")
public String logoutTest(HttpSession session) {
    session.invalidate();              // 세션 삭제
    return "first/loginResult";
}
```

**Session** : 사용자별 정보를 일정 시간 동안 **서버**에 보관하는 공간 (key-value)

> 세션은 **사물함**, `JSESSIONID`는 **열쇠 번호**.
> 정보는 서버의 사물함에 있고, 브라우저는 열쇠 번호(쿠키)만 들고 다닌다.

### 동작과정

```
1. 사용자가 처음 서버에 요청
2. 서버가 사용자 전용 세션을 만듦 (JSESSIONID : 식별키)
3. 응답할 때 JSESSIONID 쿠키 전달
4. 브라우저가 이후 요청마다 JSESSIONID 쿠키를 전송
5. 서버가 쿠키 번호로 그 사용자의 세션을 찾아서 꺼냄
```

```
서버의 세션 저장소
JSESSIONID        보관된 값
a12345      →     id = "bear"    (기본 크롬)
b12345      →     id = "tiger"   (시크릿탭 크롬)
```

- 같은 크롬의 탭끼리는 쿠키를 공유하므로 세션도 하나다. 시크릿탭은 쿠키 저장소가 따로라 **새 세션**이 만들어진다.
- 쿠키(`JSESSIONID`)를 지우면 서버의 세션을 찾아갈 수 없어서 로그인이 풀린다.
- 로그아웃(`invalidate()`)은 세션과 안에 담긴 값을 모두 삭제한다. 이후 `${session.id}`는 `null`
- 확인 : 개발자도구 → Application → Cookies → `JSESSIONID`

### Thymeleaf로 세션 값 출력

```html
<h3 th:if="${session.id != null}" th:text="|${session.id}님 환영합니다.|"></h3>
```

| 문법 | 의미 |
|---|---|
| `th:if="조건"` | 조건이 참일 때만 태그를 그린다 |
| `${ ... }` | 서버가 넘겨준 값을 꺼내는 표현식 |
| `session.id` | 세션에 `"id"`로 저장한 값 (세션 번호가 아님) |
| `\|...\|` | 문자열과 값을 이어 붙이는 문법 |

> `th:` 문법은 `templates/` 파일에서만 동작한다. `static/index.html`에서는 무시된다.

## 5. @RequestBody - 요청 본문 통째로 받기

```java
@PostMapping("body")
public void bodyTest(@RequestBody String body,
                     @RequestHeader("content-type") String contentType,
                     @CookieValue(value = "JSESSIONID", required = false) String sessionId) {

    System.out.println("body : " + URLDecoder.decode(body, StandardCharsets.UTF_8));
    System.out.println("contentType = " + contentType);
    System.out.println("sessionId = " + sessionId);
}
```

실행 결과

```
body : name=피자&price=1000&categoryCode=1&orderableStatus=y
contentType = application/x-www-form-urlencoded
sessionId = 400DA1739AD6C747B5B431A7E61F27E5
```

- 일반 form 요청의 본문은 `name=피자&price=1000&...` 같은 **쿼리 문자열** 형태다.
- 한글은 URL 인코딩(`%ED%94%BC...`)되어 오므로 `URLDecoder`로 풀어서 출력한다.
- 본문이 **JSON**(`Content-Type: application/json`)이면 Jackson 메시지 컨버터가 객체로 변환해 준다.
  ```java
  public void bodyTest(@RequestBody MenuDTO menu) { ... }
  ```

### 요청에서 값을 꺼내는 어노테이션

| 어노테이션 | 꺼내는 곳 |
|---|---|
| `@RequestParam` | 요청 파라미터 (하나씩) |
| `@ModelAttribute` | 요청 파라미터 (객체로) |
| `@RequestBody` | 요청 본문 |
| `@RequestHeader` | 요청 헤더 |
| `@CookieValue` | 쿠키 |

## 파라미터 받는 방법 비교

| | HttpServletRequest | @RequestParam | @ModelAttribute | @RequestBody |
|---|---|---|---|---|
| 받는 단위 | 하나씩 꺼냄 | 하나씩 | 객체 하나 | 본문 전체 |
| 타입 변환 | 직접 (`parseInt`) | 자동 | 자동 | 문자열 그대로 / JSON → 객체 |
| Model 자동 추가 | X | X | **O** | X |
| 주 사용처 | 요청 정보 확인 | 값이 적을 때 | 폼 값이 많을 때 | JSON API (fetch, React 등) |
