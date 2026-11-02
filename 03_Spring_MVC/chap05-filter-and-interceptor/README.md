# chap05-filter-and-interceptor

요청이 컨트롤러에 도착하기 **전**과 **후**에 끼어드는 **Filter**를 직접 만들어 보는 실습.
`StopWatchFilter`로 요청 하나를 처리하는 데 걸린 시간을 재고, 전처리 → 컨트롤러 → 후처리 순서를 콘솔 로그로 확인한다.

- Spring Boot 4.1.2 / Java 21
- Thymeleaf, Spring Web MVC

> 현재는 **Filter**까지 진행. Interceptor는 이후 추가 예정.

## 핵심 정리

| 파일 | 역할 | 비유 |
|---|---|---|
| `StopWatchFilter` | **무엇을** 할지: 요청 앞뒤에서 시간 재기 | 경비원 |
| `FilterConfiguration` | **어디서** 할지: `/stopwatch`, `/filter/*` 에만 적용 | 경비원 배치표 |

> 필터 클래스만 만들고 `FilterConfiguration`에 등록하지 않으면 **에러 없이 그냥 실행이 안 된다.**
> 로그가 안 찍히면 에러를 찾기 전에 **등록 여부와 URL 패턴**부터 확인한다.

## 요청 흐름

```
브라우저: /stopwatch 요청
   │
   ▼
① StopWatchFilter       [2.Filter] 전처리      startTime 기록
   │  chain.doFilter()   다음 필터 또는 DispatcherServlet으로 넘김
   ▼
② DispatcherServlet     @GetMapping("/stopwatch") 가 붙은 메서드를 찾아 호출
   ▼
③ StopWatchController   [3.Controller] handlerMethod 호출 → 2초 대기
                        model에 수행시간 담고 return "result"
   ▼
④ result.html           ${수행시간} 자리에 "2초" 출력
   │
   ▼
⑤ StopWatchFilter       [4.Filter] 후처리      endTime 기록
                        전체 체인 수행 시간 = endTime - startTime
```

`stopwatch()`를 직접 호출하는 코드는 없다. **Spring(DispatcherServlet)이 주소를 보고 대신 호출**한다.
이렇게 요청을 처리하려고 호출되는 컨트롤러 메서드를 **handlerMethod**라고 부른다.

## 디렉토리 구조

```
src/main
├── java/org/example/filterandinterceptor
│   ├── Chap05FilterAndInterceptorApplication.java
│   ├── controller
│   │   ├── MainController.java          # "/"  → main.html
│   │   ├── StopWatchController.java     # "/stopwatch" → 2초 대기 후 result.html
│   │   └── FilterTestController.java    # "/filter/block" → 문자열 응답
│   └── filter
│       ├── StopWatchFilter.java         # 요청 처리 시간 측정 필터
│       └── FilterConfiguration.java     # 필터 등록 (URL 패턴, 순서)
└── resources
    ├── application.yaml
    └── templates
        ├── main.html                    # StopWatch 버튼
        └── result.html                  # 수행시간 출력 + 돌아가기 버튼
```

## 실습 경로

| 요청 | 필터 실행 | 결과 |
|---|---|---|
| `GET /` | ❌ (등록된 패턴 아님) | `main.html` |
| `GET /stopwatch` | ✅ | 2초 후 `result.html` |
| `GET /filter/block` | ✅ (`/filter/*`) | `"Filter를 통과하여 Controller가 실행되었습니다."` |

## 1. StopWatchFilter: 무엇을 할지

`jakarta.servlet.Filter`를 구현한다.

| 메서드 | 실행 시점 | 하는 일 |
|---|---|---|
| `init()` | 서버 시작 시 **한 번** | `[1.Filter] init` 출력 |
| `doFilter()` | 요청이 올 **때마다** | 전처리 → 다음 단계 → 후처리 |
| `destroy()` | 서버 종료 시 **한 번** | `[99. Filter] destroy` 출력 |

```java
@Override
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

    // ===== 전처리 : 컨트롤러 실행 "전" =====
    System.out.println("[2.Filter] 전처리 시간 측정");
    long startTime = System.currentTimeMillis();

    // 다음 필터 또는 DispatcherServlet으로 요청 전달
    // -> 컨트롤러가 끝날 때까지 여기서 기다림
    chain.doFilter(request, response);

    // ===== 후처리 : 컨트롤러 실행 "후" =====
    System.out.println("[4.Filter] 후처리");
    long endTime = System.currentTimeMillis();

    System.out.println("[4-1.Filter] - 전체 체인 수행 시간 : " + (endTime - startTime) + "ms");
}
```

- 필터는 **하나**이고, `chain.doFilter()` **위쪽이 전처리, 아래쪽이 후처리**다.
- `chain.doFilter()`를 호출하지 않으면 요청이 컨트롤러까지 가지 못한다. (= 요청 차단)

## 2. FilterConfiguration: 어디서 할지

Filter는 서블릿 기술이라 서블릿 컨테이너에 등록해야 한다.
`FilterRegistrationBean`을 `@Bean`으로 등록하면 Spring이 대신 등록해 준다.

```java
@Configuration
public class FilterConfiguration {

    @Bean
    public FilterRegistrationBean<StopWatchFilter> stopWatchFilter() {
        FilterRegistrationBean<StopWatchFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new StopWatchFilter());              // 등록할 필터
        registration.addUrlPatterns("/stopwatch", "/filter/*");     // 적용할 URL
        registration.setOrder(1);                                   // 실행 순서 (작을수록 먼저)
        registration.setName("stopWatchFilter");                    // 필터 이름
        return registration;
    }
}
```

| 주소 | 필터 실행? | 이유 |
|---|---|---|
| `/stopwatch` | ✅ | 패턴에 그대로 있음 |
| `/filter/block` | ✅ | `/filter/*`의 `*`는 "뒤에 무엇이 와도 됨" |
| `/` | ❌ | 패턴에 없음 |

## 3. 컨트롤러

### StopWatchController: `@Controller` → html 화면

```java
@GetMapping("/stopwatch")
public String stopwatch(Model model) throws InterruptedException {
    System.out.println("[3.Controller] handlerMethod 호출");

    Thread.sleep(1000);
    System.out.println("[3.Controller] 1초 경과");

    Thread.sleep(1000);
    System.out.println("[3.Controller] 2초 경과");

    model.addAttribute("수행시간", "2초");
    return "result";   // templates/result.html
}
```

- `Thread.sleep()`은 `InterruptedException`을 던질 수 있으므로 `throws`를 붙여야 컴파일된다.
- `model.addAttribute("수행시간", ...)`의 키와 `result.html`의 `${수행시간}` 이름이 같아야 값이 출력된다.

### FilterTestController: `@RestController` → 문자열 그대로

```java
@RestController
@RequestMapping("/filter")
public class FilterTestController {

    @GetMapping("/block")      // /filter + /block = /filter/block
    public String block() {
        return "Filter를 통과하여 Controller가 실행되었습니다.";
    }
}
```

문구가 **보이면** 필터가 통과시킨 것이고, **안 보이면** 필터가 막은 것이다.

| | `@Controller` | `@RestController` |
|---|---|---|
| `return "result"`의 의미 | `result.html` **파일**을 찾아 보여줌 | `"result"` **글자**를 그대로 응답 |
| 사용 | `MainController`, `StopWatchController` | `FilterTestController` |

## 4. Thymeleaf 값 출력

```html
<p th:text="${수행시간}">인터셉터</p>
```

- `th:text` 안은 일반 글자가 아니라 **식**이다.
  - 글자 그대로: `th:text="'수행시간'"` (작은따옴표)
  - Model 값: `th:text="${수행시간}"`
- Model에 해당 키가 없으면 기본 글자(`인터셉터`)가 **빈 값으로 바뀐다.**

## 콘솔 로그 (`/stopwatch` 요청 시)

```
[1.Filter] StopWatchFilter init - 어플리케이션 시작 시 한 번 호출     ← 서버 시작 시
[2.Filter] 전처리 시간 측정
[3.Controller] handlerMethod 호출
[3.Controller] 1초 경과
[3.Controller] 2초 경과
[4.Filter] 후처리
[4-1.Filter] - 전체 체인 수행 시간 : 2010ms
[99. Filter] destroy - 어플리케이션 종료 시 한 번 호출                 ← 서버 종료 시
```
