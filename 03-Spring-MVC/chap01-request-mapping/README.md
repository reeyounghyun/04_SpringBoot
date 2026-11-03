# chap01-request-mapping

요청 URL과 HTTP 메소드를 컨트롤러의 **핸들러 메소드**에 연결하는 방법(요청 매핑)을 익히는 실습.
메소드 단위 매핑 → HTTP 메소드 제한 → 클래스 단위 매핑 → 여러 URL 매핑 → `@PathVariable` 순서로 진행한다.

- Spring Boot 4.1.2 / Java 21
- Thymeleaf, Spring Web MVC, devtools
- 서버 포트 : **8081** (`application.yaml`) → http://localhost:8081

## 공통 흐름

```
① index.html 버튼/폼 클릭     GET 또는 POST 요청 (/menu/xxx, /order/xxx)
        ↓
② DispatcherServlet이 URL + HTTP 메소드에 맞는 핸들러 메소드를 찾음
        ↓
③ Model에 값 담기             model.addAttribute("message", ...)
        ↓
④ 결과 화면 반환               return "mappingResult" → templates/mappingResult.html
```

> 모든 핸들러 메소드가 같은 결과 화면(`mappingResult.html`)을 쓰고, `message` 값만 바꿔서 넘긴다.

## 디렉토리 구조

```
src/main
├── java/org/example/requestmapping
│   ├── Chap01RequestMappingApplication.java   # 실행 클래스 (@SpringBootApplication)
│   ├── chap01
│   │   └── RequestMappingApplication.java     # 메소드에 요청 매핑 (/menu/...)
│   └── chap02
│       └── ClassMappingTestController.java    # 클래스에 요청 매핑 (/order/...)
└── resources
    ├── application.yaml                       # 포트 8081, Thymeleaf 캐시 끔
    ├── static
    │   └── index.html                         # 실습 메뉴 (정적 파일)
    └── templates
        └── mappingResult.html                 # 결과 메시지 출력 (${ message })
```

> `@SpringBootApplication`이 있는 패키지(`org.example.requestmapping`)와 그 **하위 패키지**만 컴포넌트 스캔된다.
> 그래서 `chap01`, `chap02` 패키지의 `@Controller`도 자동으로 등록된다.

## 1. 메소드에 요청 매핑하기 - @RequestMapping

```java
@RequestMapping("/menu/regist")
public String registMenu(Model model) {
    model.addAttribute("message", "신규 등록한 핸들러메소드 호출함");
    return "mappingResult";
}
```

- URL만 적으면 **모든 HTTP 메소드**(GET, POST, PUT, DELETE ...)를 이 메소드 하나가 처리한다.
- 그래서 `GET /menu/regist`, `POST /menu/regist` 둘 다 같은 결과가 나온다.

## 2. HTTP 메소드 지정하기 - @GetMapping

```java
// @RequestMapping(value = "/menu/modify", method = RequestMethod.GET) 과 같음
@GetMapping("/menu/modify")
public String modifyMenu(Model model) { ... }
```

| 요청 | 결과 |
|---|---|
| `GET /menu/modify` | 정상 처리 |
| `POST /menu/modify` | **405 Method Not Allowed** (매핑된 메소드가 없음) |

### 요청 전용 어노테이션

| 어노테이션 | 같은 의미 |
|---|---|
| `@GetMapping` | `@RequestMapping(method = RequestMethod.GET)` |
| `@PostMapping` | `@RequestMapping(method = RequestMethod.POST)` |
| `@PutMapping` | `@RequestMapping(method = RequestMethod.PUT)` |
| `@DeleteMapping` | `@RequestMapping(method = RequestMethod.DELETE)` |
| `@PatchMapping` | `@RequestMapping(method = RequestMethod.PATCH)` |

> HTML `<form>`은 `GET`, `POST`만 보낼 수 있다. 버튼의 `location.href` 이동은 항상 `GET`이다.

## 3. 클래스에 요청 매핑하기

```java
@Controller
@RequestMapping("/order")
public class ClassMappingTestController {

    @GetMapping("/regist")          // → GET /order/regist
    public String registOrder(Model model) { ... }
}
```

- 클래스의 `@RequestMapping` 경로가 **공통 앞부분**이 되고, 메소드 경로가 뒤에 붙는다.
- 같은 기능(주문)의 URL을 한 컨트롤러에 모을 때 사용한다.

## 4. 여러 URL을 하나의 메소드에 매핑하기

```java
@RequestMapping(value = {"modify", "delete"}, method = {RequestMethod.POST})
public String modifyAndDelete(Model model) { ... }
```

- `value`에 중괄호 `{ }`로 URL을 나열하면 `POST /order/modify`, `POST /order/delete` 둘 다 이 메소드가 처리한다.
- `method`도 배열로 여러 개 지정할 수 있다.

## 5. URL에서 값 꺼내기 - @PathVariable

```java
@GetMapping("/detail/{orderNo}")
public String selectOrderDetail(Model model, @PathVariable("orderNo") int orderNo) {
    model.addAttribute("message", orderNo + "번 주문 상세조회합니다.");
    return "mappingResult";
}
```

```
GET /order/detail/5   →   orderNo = 5   →   "5번 주문 상세조회합니다."
```

- URL의 `{변수명}` 자리 값을 매개변수로 받는다. 문자열 → `int` 변환도 자동이다.
- 매개변수 이름과 `{ }` 안 이름이 같으면 `@PathVariable int orderNo`처럼 이름을 생략할 수 있다.
- 숫자가 아닌 값(`/order/detail/abc`)을 보내면 변환 실패로 **400** 오류

## 전체 매핑 정리

| 요청 | 처리 메소드 | 비고 |
|---|---|---|
| `GET`, `POST` `/menu/regist` | `registMenu` | 메소드 제한 없음 |
| `GET /menu/modify` | `modifyMenu` | `POST`는 405 |
| `GET /order/regist` | `registOrder` | 클래스 매핑 `/order` + `/regist` |
| `POST /order/modify`, `/order/delete` | `modifyAndDelete` | URL 여러 개 |
| `GET /order/detail/{orderNo}` | `selectOrderDetail` | `@PathVariable` |

## 실행

```bash
./gradlew bootRun
```

브라우저에서 http://localhost:8081 접속 → `static/index.html`의 버튼으로 각 요청을 보내 본다.
