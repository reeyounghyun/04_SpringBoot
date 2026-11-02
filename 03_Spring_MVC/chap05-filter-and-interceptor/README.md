# chap05-filter-and-interceptor

요청이 컨트롤러에 도착하기 **전**과 **후**에 끼어드는 **Filter**와 **Interceptor**를 직접 만들어 비교하는 실습.
둘 다 요청 처리 시간을 재고, 조건이 맞지 않는 요청을 **403으로 차단**해 본다.

- Spring Boot 4.1.2 / Java 21
- Thymeleaf, Spring Web MVC

## 핵심 정리

| | Filter | Interceptor |
|---|---|---|
| **무엇을** (일하는 클래스) | `StopWatchFilter` | `StopWatchInterceptor` |
| **어디서** (등록 설정) | `FilterConfiguration` | `WebConfiguration` |
| 소속 | 서블릿 (Spring **밖**) | Spring MVC (DispatcherServlet **안**) |
| 구현 | `jakarta.servlet.Filter` | `HandlerInterceptor` |
| 전처리 | `chain.doFilter()` **위** | `preHandle()` |
| 후처리 | `chain.doFilter()` **아래** | `postHandle()`, `afterCompletion()` |
| 통과 / 차단 | `chain.doFilter()` 호출 / `return;` | `return true` / `return false` |
| 요청 타입 | `ServletRequest` → 형변환 필요 | 처음부터 `HttpServletRequest` |
| 컨트롤러 정보, ModelAndView | 모름 | 사용 가능 |

> 필터든 인터셉터든 클래스만 만들고 **등록하지 않으면 에러 없이 그냥 실행이 안 된다.**
> 로그가 안 찍히면 에러를 찾기 전에 **등록 여부와 URL 패턴**부터 확인한다.

## 요청 흐름 (`/stopwatch`)

```
브라우저: /stopwatch 요청
   │
   ▼
StopWatchFilter          [2.Filter] 전처리              startTime 기록
   │  chain.doFilter()
   ▼
DispatcherServlet        @GetMapping("/stopwatch") 메서드를 찾음
   ▼
StopWatchInterceptor     [3.Interceptor] preHandle()    request에 interceptorStartTime 저장
   ▼
StopWatchController      [4.Controller] 2초 대기 → return "result"
   ▼
StopWatchInterceptor     [5.Interceptor] postHandle()   걸린 시간을 modelAndView에 담음
   ▼
result.html 렌더링       "인터셉터 수행시간 : 2003 ms"
   ▼
StopWatchInterceptor     [6.Interceptor] afterCompletion()  화면까지 끝난 뒤
   ▼
StopWatchFilter          [7.Filter] 후처리              전체 체인 수행 시간 출력
```

- 필터는 인터셉터를 **바깥에서 감싼다.** 그래서 필터 시간이 인터셉터 시간보다 조금 더 길다.
- `stopwatch()`를 직접 호출하는 코드는 없다. **DispatcherServlet이 주소를 보고 대신 호출**한다.
  이렇게 호출되는 컨트롤러 메서드를 **handlerMethod**라고 부른다.

## 디렉토리 구조

```
src/main
├── java/org/example/filterandinterceptor
│   ├── Chap05FilterAndInterceptorApplication.java
│   ├── controller
│   │   ├── MainController.java               # "/"  → main.html
│   │   ├── StopWatchController.java          # "/stopwatch" → 2초 대기 후 result.html
│   │   ├── FilterTestController.java         # "/filter/block" → 문자열 응답
│   │   └── InterceptorTestController.java    # "/interceptor/block" → 문자열 응답
│   ├── filter
│   │   ├── StopWatchFilter.java              # 시간 측정 + X-Test 헤더 검사
│   │   └── FilterConfiguration.java          # 필터 등록 (URL 패턴, 순서)
│   └── interceptor
│       ├── StopWatchInterceptor.java         # 시간 측정 + access 파라미터 검사
│       └── WebConfiguration.java             # 인터셉터 등록 (WebMvcConfigurer)
└── resources
    ├── application.yaml
    └── templates
        ├── main.html                         # 실습 버튼 4개
        └── result.html                       # 인터셉터 수행시간 출력
```

> `/filter/block`, `/interceptor/block`은 **파일 경로가 아니라 주소**다.
> `block`이라는 파일은 없고, 컨트롤러의 `@RequestMapping` + `@GetMapping`이 합쳐져 만들어진다.

## 실습 경로

| 요청 | Filter | Interceptor | 결과 |
|---|---|---|---|
| `GET /` | ❌ | ❌ | `main.html` |
| `GET /stopwatch` | ✅ | ✅ | 2초 후 `result.html` + 인터셉터 수행시간 |
| `GET /filter/block` | ✅ **차단** | ❌ | 403 "X-Test 헤더가 필요합니다." |
| `GET /filter/block` + `X-Test` 헤더 (Postman) | ✅ 통과 | ❌ | "Filter를 통과하여 Controller가 실행되었습니다." |
| `GET /interceptor/block` | ❌ | ✅ **차단** | 403 "access=granted 파라미터가 필요합니다." |
| `GET /interceptor/block?access=granted` | ❌ | ✅ 통과 | "Interceptor를 통과하여 Controller가 실행되었습니다." |

---

# Part 1. Filter

## 1-1. StopWatchFilter: 무엇을 할지

| 메서드 | 실행 시점 | 하는 일 |
|---|---|---|
| `init()` | 서버 시작 시 **한 번** | `[1.Filter] init` 출력 |
| `doFilter()` | 요청이 올 **때마다** | 차단 검사 → 전처리 → 다음 단계 → 후처리 |
| `destroy()` | 서버 종료 시 **한 번** | `[99. Filter] destroy` 출력 |

```java
@Override
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

    // ServletRequest/ServletResponse는 HTTP 전용 타입이 아님
    // -> URI, HEADER, 상태코드(HTTP)를 쓰기 위해 변환
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    // X-Test 헤더가 없는 /filter/block 요청 차단
    if (httpRequest.getRequestURI().equals("/filter/block")
            && httpRequest.getHeader("X-Test") == null) {
        httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "X-Test 헤더가 필요합니다.");
        return; // chain.doFilter()를 호출하지 않으므로 DispatcherServlet, Controller가 실행되지 않음
    }

    // ===== 전처리 =====
    long startTime = System.currentTimeMillis();

    chain.doFilter(request, response); // 컨트롤러가 끝날 때까지 여기서 기다림

    // ===== 후처리 =====
    long endTime = System.currentTimeMillis();
    System.out.println("[7-1.Filter] - 전체 체인 수행 시간 : " + (endTime - startTime) + "ms");
}
```

- 필터는 **하나**이고, `chain.doFilter()` **위쪽이 전처리, 아래쪽이 후처리**다.
- `(HttpServletRequest) request`처럼 **형변환**해야 `getRequestURI()`, `getHeader()`를 쓸 수 있다.
- `sendError(403, ...)`만 하고 `return;`을 빼면 아래 `chain.doFilter()`까지 진행돼 **차단되지 않는다.**

| 메서드 | `/filter/block` 접속 시 |
|---|---|
| `getRequestURI()` (끝이 **I**) | `/filter/block` |
| `getRequestURL()` (끝이 **L**) | `http://localhost:8080/filter/block` |

## 1-2. FilterConfiguration: 어디서 할지

Filter는 서블릿 기술이라 서블릿 컨테이너에 등록해야 한다.
`FilterRegistrationBean`을 `@Bean`으로 등록하면 Spring이 대신 등록해 준다.

```java
@Bean
public FilterRegistrationBean<StopWatchFilter> stopWatchFilter() {
    FilterRegistrationBean<StopWatchFilter> registration = new FilterRegistrationBean<>();
    registration.setFilter(new StopWatchFilter());              // 등록할 필터
    registration.addUrlPatterns("/stopwatch", "/filter/*");     // 적용할 URL
    registration.setOrder(1);                                   // 실행 순서 (작을수록 먼저)
    registration.setName("stopWatchFilter");                    // 필터 이름
    return registration;
}
```

`/filter/*`의 `*`는 "뒤에 무엇이 와도 됨"이라는 뜻이다.

---

# Part 2. Interceptor

## 2-1. StopWatchInterceptor: 무엇을 할지

`HandlerInterceptor`를 구현하고 `@Component`로 Bean 등록한다.

| 메서드 | 실행 시점 | 하는 일 |
|---|---|---|
| `preHandle()` | 컨트롤러 실행 **전** | 차단 검사, 시작 시각 저장 |
| `postHandle()` | 컨트롤러 실행 **후**, 화면 렌더링 **전** | 걸린 시간을 `modelAndView`에 추가 |
| `afterCompletion()` | 화면 렌더링까지 **모두 끝난 뒤** (예외 정보도 받음) | 로그 출력 |

### preHandle(): 차단 + 시작 시각 저장

```java
@Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    // access=granted 파라미터가 없는 /interceptor/block 요청 차단
    if (request.getRequestURI().equals("/interceptor/block")
            && !"granted".equals(request.getParameter("access"))) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "access=granted 파라미터가 필요합니다.");
        return false; // 컨트롤러 실행 안 됨
    }

    request.setAttribute("interceptorStartTime", System.currentTimeMillis());
    return true;      // 다음 단계(컨트롤러)로 진행
}
```

- `"granted".equals(request.getParameter("access"))` 순서로 쓴다.
  파라미터가 없으면 `getParameter()`가 `null`이라, 반대로 쓰면 `NullPointerException`이 난다.
- 시작 시각을 **`request`에 저장**하는 이유:
  필터는 전처리/후처리가 한 메서드 안이라 지역 변수로 충분했지만,
  인터셉터는 `preHandle` → `postHandle`이 **다른 메서드**라서 지역 변수가 넘어가지 않는다.
  `request`는 요청 하나가 끝날 때까지 같은 객체이므로 여기에 값을 붙여 둔다.

### postHandle(): 걸린 시간을 화면으로 전달

```java
@Override
public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                       @Nullable ModelAndView modelAndView) throws Exception {
    long startTime = (long) request.getAttribute("interceptorStartTime");
    long interval = System.currentTimeMillis() - startTime;

    // @RestController 처럼 화면이 없는 응답은 modelAndView가 null
    if (modelAndView != null) {
        modelAndView.addObject("interceptorInterval", interval);
    }
}
```

- `getAttribute()`는 `Object`를 돌려주므로 `(long)`으로 형변환한다.
- `setAttribute`와 `getAttribute`의 **이름이 한 글자라도 다르면** 에러 없이 `null`이 나온다.
- `/interceptor/block`은 `@RestController`라 `modelAndView`가 `null`이다.
  `null` 체크가 없으면 통과 요청에서 **500 에러**가 난다.

## 2-2. WebConfiguration: 어디서 할지

`WebMvcConfigurer`는 Spring MVC 설정(CORS, 인터셉터, URL 매핑 등)을 개발자가 커스텀할 수 있게 해 주는 인터페이스다.
필요한 메서드만 골라서 오버라이드하면 되고, 인터셉터는 `addInterceptors()`에서 등록한다.

```java
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private StopWatchInterceptor stopWatchInterceptor;

    // 생성자 주입
    public WebConfiguration(StopWatchInterceptor stopWatchInterceptor) {
        this.stopWatchInterceptor = stopWatchInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(stopWatchInterceptor)                                   // 무엇을
                .addPathPatterns("/stopwatch", "/interceptor/**")                       // 어디서
                .excludePathPatterns("/css/**", "/images/**", "/js/**", "/error");      // 제외
    }
}
```

| 설정 | 의미 |
|---|---|
| `addInterceptor()` | 등록할 인터셉터 (끝에 **s 없음**. `addInterceptors`는 오버라이드한 메서드 이름) |
| `addPathPatterns()` | 인터셉터를 **실행할** 주소 |
| `excludePathPatterns()` | 인터셉터를 **실행하지 않을** 주소 (정적 파일, 에러 페이지) |

`/**`는 "그 아래 몇 단계든 전부"라는 뜻이다.

> `@Component`는 인터셉터 객체를 Spring에 만들어 두기만 한다.
> **어느 주소에 적용할지**는 `WebConfiguration`에 등록해야 정해진다.

---

# Part 3. 컨트롤러와 화면

## 테스트용 컨트롤러

```java
@RestController                    // 화면이 아닌 문자를 리턴
@RequestMapping("/interceptor")
public class InterceptorTestController {

    @GetMapping("/block")          // /interceptor + /block = /interceptor/block
    public String block() {
        return "Interceptor를 통과하여 Controller가 실행되었습니다.";
    }
}
```

`FilterTestController`도 같은 모양(`/filter/block`)이다.
문구가 **보이면** 통과, **안 보이고 403이 뜨면** 차단된 것이다.

| | `@Controller` | `@RestController` |
|---|---|---|
| `return "result"`의 의미 | `templates/result.html` **파일**을 찾아 보여줌 | `"result"` **글자**를 그대로 응답 |
| `postHandle()`의 `modelAndView` | 있음 | `null` |
| 사용 | `MainController`, `StopWatchController` | `FilterTestController`, `InterceptorTestController` |

## Thymeleaf 값 출력

```html
<p th:text="|인터셉터 수행시간 : ${interceptorInterval} ms|">
    Interceptor 수행시간
</p>
```

- `th:text` 안은 일반 글자가 아니라 **식**이다.

| 쓰는 방법 | 결과 |
|---|---|
| `th:text="인터셉터 수행시간 : ${interceptorInterval}"` | ❌ 에러 |
| `th:text="\|인터셉터 수행시간 : ${interceptorInterval}\|"` | ✅ 글자 + 값 (리터럴 치환) |
| `th:text="'인터셉터 수행시간 : ' + ${interceptorInterval}"` | ✅ 같은 결과 |

- 태그 안의 `Interceptor 수행시간`은 html 파일을 직접 열 때만 보이고, 서버를 거치면 `th:text` 값으로 바뀐다.

## main.html 버튼

| 버튼 | 이동 주소 | 결과 |
|---|---|---|
| 수행 시간 확인하기 | `/stopwatch` | 2초 후 `result.html` |
| Filter Block 확인하기 | `/filter/block` | 403 |
| Interceptor Block 확인하기 | `/interceptor/block` | 403 |
| Interceptor Block 안되는것 확인 | `/interceptor/block?access=granted` | 통과 문구 |

> 필터 통과는 `X-Test` **헤더**가 필요해서 브라우저 버튼으로는 확인할 수 없다. Postman으로 헤더를 넣어 요청한다.

---

## 콘솔 로그 (`/stopwatch` 요청 시)

```
[1.Filter] StopWatchFilter init - 어플리케이션 시작 시 한 번 호출     ← 서버 시작 시
httpRequest.getRequestURI() : /stopwatch
[2.Filter] 전처리 시간 측정
[3.Interceptor] preHandle - 컨트롤러 호출 전 !
[4.Controller] handlerMethod 호출
[4.Controller] 1초 경과
[4.Controller] 2초 경과
[5.Interceptor] postHandle - 컨트롤러 호출 후, 뷰 랜더링 전 !
[6.Interceptor] afterCompletion - 뷰 랜더링 후 !
[7.Filter] 후처리
[7-1.Filter] - 전체 체인 수행 시간 : 2010ms
[99. Filter] destroy - 어플리케이션 종료 시 한 번 호출                 ← 서버 종료 시
```

> `2010ms`는 예시이며 실행할 때마다 조금씩 다르다.
