# chap03-view-resolver

핸들러 메소드가 반환한 값을 **ViewResolver**가 어떻게 화면으로 연결하는지 보는 실습.
`String`과 `ModelAndView` 두 가지 반환 방식으로 **forward**, **redirect**, **flash attribute**를 각각 비교한다.

- Spring Boot 4.1.2 / Java 21
- Thymeleaf, Spring Web MVC, devtools

## 공통 흐름

```
① 핸들러 메소드가 '논리 뷰 이름' 반환     return "result";
        ↓
② ViewResolver가 실제 파일 경로로 변환    prefix + "result" + suffix
        ↓
③ templates/result.html 을 찾음          classpath:/templates/result.html
        ↓
④ Thymeleaf가 Model 값을 채워 응답        ${ forwardMessage }
```

> `@Controller` 메소드가 반환하는 문자열은 **응답 내용이 아니라** ViewResolver가 해석할 **논리 뷰 이름**이다.
> (문자열 그대로 응답하려면 `@ResponseBody` / `@RestController`를 쓴다.)

## 디렉토리 구조

```
src/main
├── java/org/example/viewresolver
│   ├── Chap03ViewResolverApplication.java
│   ├── MainController.java         # "/", "/main" → main.html
│   └── ResolverController.java     # String / ModelAndView 반환 실습 전체
└── resources
    ├── application.yaml
    └── templates
        ├── main.html               # 실습 메뉴 + flash 메시지 alert
        └── result.html             # forward 결과 메시지 출력
```

## ViewResolver 설정

```yaml
spring:
  application:
    name: chap03-view-resolver

##thymeleaf starter의 자동 설정으로 인해 작성 안해도 된다.
#  thymeleaf:
#    prefix: classpath:/templates/
#    suffix: .html
```

| 설정 | 기본값 | 의미 |
|---|---|---|
| `prefix` | `classpath:/templates/` | 뷰 이름 **앞**에 붙는 경로 |
| `suffix` | `.html` | 뷰 이름 **뒤**에 붙는 확장자 |

`spring-boot-starter-thymeleaf`가 위 값으로 자동 설정하므로 직접 쓰지 않아도 된다.
폴더나 확장자를 바꾸고 싶을 때만 작성한다.

## 실습 경로

| 요청 | 반환 방식 | 결과 |
|---|---|---|
| `GET /` , `/main` | `String` | `main.html` |
| `GET /string` | `String` | forward → `result.html` |
| `GET /string-redirect` | `String` | redirect → `/` |
| `GET /string-redirect-attr` | `String` + `RedirectAttributes` | redirect → `/` + alert |
| `GET /modelandview` | `ModelAndView` | forward → `result.html` |
| `GET /modelandview-redirect` | `ModelAndView` | redirect → `/` |
| `GET /modelandview-redirect-attr` | `ModelAndView` + `RedirectAttributes` | redirect → `/` + alert |

## 1. String으로 뷰 이름 반환

```java
@GetMapping("string")
public String stringReturning(Model model) {
    model.addAttribute("forwardMessage", "문자열로 뷰 이름 반환함...");
    return "result";   // templates/result.html
}
```

- 데이터는 `Model`에, 뷰 이름은 **반환값**으로 따로 전달한다.
- **forward** : 서버 안에서 화면만 바꿔 응답한다. 브라우저 주소는 `/string` 그대로이고 Model 값이 유지된다.

## 2. String으로 redirect

```java
@GetMapping("string-redirect")
public String stringRedirect() {
    return "redirect:/";
}
```

- `redirect:` 접두사가 붙으면 ViewResolver가 파일을 찾지 않고 **브라우저에게 다시 요청하라고** 응답한다. (302 + `Location: /`)
- 브라우저가 `/`로 **새 요청**을 보내므로 주소창이 바뀌고, 이전 요청의 Model 값은 사라진다.

### forward vs redirect

| | forward | redirect |
|---|---|---|
| 요청 횟수 | 1번 | 2번 (브라우저가 다시 요청) |
| 주소창 | 그대로 | 바뀜 |
| Model 값 | 유지 | **사라짐** |
| 주 사용처 | 조회 결과 보여주기 | 등록·수정 후 새로고침 중복 전송 방지 |

## 3. RedirectAttributes - flash attribute

```java
@GetMapping("string-redirect-attr")
public String stringRedirectFlashAttribute(RedirectAttributes rAttr) {
    rAttr.addFlashAttribute("flashMessage1", "RedirectAttribute를 사용해 redirect..");
    return "redirect:/";
}
```

**flash attribute** : 리다이렉트 대상의 다음 요청에 자동으로 전달된다.
한 번 사용한 뒤 제거되는 **일회용 데이터**

- redirect하면 Model 값이 사라지기 때문에, 잠깐 세션에 보관했다가 다음 요청의 Model로 넘겨준다.
- 메시지를 확인한 뒤 새로고침하면 alert가 다시 뜨지 않는다. (이미 제거됨)

### main.html에서 꺼내기

```html
<script>
    const flashMessage1 = '[[${ flashMessage1 }]]';
    if (flashMessage1) {
        alert(flashMessage1);
    }
</script>
```

- `[[${ ... }]]` : Thymeleaf **인라인 표현식**. `<script>` 안에 서버 값을 문자열로 넣는다.
- 값이 없으면 빈 문자열 `''`이 되어 `if`에 걸리지 않는다.

## 4. ModelAndView로 뷰 이름 반환

```java
@GetMapping("modelandview")
public ModelAndView modelAndViewReturning(ModelAndView mv) {
    mv.addObject("forwardMessage", "ModelAndView를 이용한 모델과 뷰 반환"); // 데이터 전달
    mv.setViewName("result"); // PATH 지정
    return mv;
}
```

- **Model(데이터) + View(뷰 이름)** 를 객체 하나에 담아 반환한다.
- `addObject()` = `model.addAttribute()`, `setViewName()` = `return "뷰이름"`

## 5. ModelAndView로 redirect / flash attribute

```java
@GetMapping("modelandview-redirect")
public ModelAndView modelAndViewRedirect(ModelAndView mv) {
    mv.setViewName("redirect:/");
    return mv;
}

@GetMapping("modelandview-redirect-attr")
public ModelAndView modelAndViewRedirect(ModelAndView mv, RedirectAttributes rttr) {
    rttr.addFlashAttribute("flashMessage2", "ModelAndview를 이용한 RedirectAttribute");
    mv.setViewName("redirect:/");
    return mv;
}
```

- 뷰 이름에 `redirect:`를 붙이는 방식은 String 반환과 같다.
- flash attribute도 똑같이 `RedirectAttributes`를 매개변수로 받아 사용한다.

## String vs ModelAndView 비교

| | String | ModelAndView |
|---|---|---|
| 데이터 | `Model` 매개변수에 담음 | `mv.addObject()` |
| 뷰 이름 | `return "result"` | `mv.setViewName("result")` |
| redirect | `return "redirect:/"` | `mv.setViewName("redirect:/")` |
| flash attribute | `RedirectAttributes` | `RedirectAttributes` |
| 특징 | 코드가 짧고 많이 쓰임 | 데이터와 뷰를 한 객체로 다룸 |
