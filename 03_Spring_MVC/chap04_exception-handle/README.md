# chap04_exception-handle

컨트롤러에서 발생한 **예외를 처리하는 방법**을 비교하는 실습.
같은 예외(NullPointerException, 사용자 정의 예외)를 `@ExceptionHandler`로 **컨트롤러 레벨**에서,
`@ControllerAdvice`로 **전역(Global) 레벨**에서 처리해 보고, `Exception` 핸들러로 마지막 안전망을 만든다.

- Spring Boot 4.1.2 / Java 21
- Thymeleaf, Spring Web MVC, devtools

## 공통 흐름

```
① main.html 버튼 클릭          GET /controller-null 등
        ↓
② 핸들러 메소드 실행 중 예외 발생   str.charAt(0) → NullPointerException
                                 throw new MemberRegistException(...)
        ↓
③ 스프링이 예외에 맞는 @ExceptionHandler 메소드를 찾음
        ↓
④ 에러 화면 반환                 return "error/nullPointer" → templates/error/nullPointer.html
```

> 예외 처리는 에러를 **발생시키는 것이 아니라**, 이미 발생한 에러를 **받아서 처리**하는 것이다.
> `@ExceptionHandler`는 "이 예외가 나면 이렇게 처리해 줘"라고 미리 등록해 두는 것으로,
> JS의 `addEventListener`처럼 예외가 발생하기 전에는 실행되지 않는다.

## 디렉토리 구조

```
src/main
├── java/org/example/exceptionhandle
│   ├── Chap04ExceptionHandleApplication.java
│   ├── controller
│   │   ├── MainController.java                # GET /, /main → main.html
│   │   ├── ExceptionHandlerController.java    # 컨트롤러 레벨 @ExceptionHandler 실습
│   │   └── OtherController.java               # 전역 처리 확인용 (자체 핸들러 없음)
│   └── exception
│       ├── MemberRegistException.java         # 사용자 정의 예외 (extends Exception)
│       └── GlobalExceptionHandler.java        # @ControllerAdvice 전역 예외 처리
└── resources/templates
    ├── main.html                              # 실습 메뉴 (버튼 목록)
    └── error
        ├── nullPointer.html                   # NullPointerException 화면
        ├── memberRegist.html                  # MemberRegistException 화면 (예외 메시지 출력)
        ├── controller-array.html              # ArrayIndexOutOfBoundsException 화면
        └── default.html                       # 그 외 모든 예외 화면
```

## 요청별 처리 결과

| 요청 | 발생 예외 | 처리하는 핸들러 | 화면 |
|---|---|---|---|
| `/controller-null` | NullPointerException | `ExceptionHandlerController` (컨트롤러 레벨) | `error/nullPointer` |
| `/controller-user` | MemberRegistException | `ExceptionHandlerController` (컨트롤러 레벨) | `error/memberRegist` |
| `/other-controller-null` | NullPointerException | `GlobalExceptionHandler` (전역) | `error/nullPointer` |
| `/other-controller-user` | MemberRegistException | `GlobalExceptionHandler` (전역) | `error/memberRegist` |
| `/other-controller-array` | ArrayIndexOutOfBoundsException | `GlobalExceptionHandler` (전역) | `error/controller-array` |
| 그 외 예외 | Exception 하위 예외 | `GlobalExceptionHandler`의 `Exception` 핸들러 (안전망) | `error/default` |

## 1. @ExceptionHandler - 컨트롤러 레벨

```java
@Controller
public class ExceptionHandlerController {

    @GetMapping("/controller-null")
    public String nullPointerException() {
        String str = null;
        System.out.println(str.charAt(0));   // NullPointerException 발생!!
        return "/";                          // 여기까지 오지 못함
    }

    @ExceptionHandler(NullPointerException.class)
    public String nullPointerExceptionHandler(NullPointerException exception) {
        System.out.println("controller 레벨의 exception 처리!!");
        return "error/nullPointer";
    }
}
```

- **메소드**에 붙여서 "지정한 예외가 발생하면 이 메소드를 실행해 줘"라고 등록한다.
- 컨트롤러 안에 작성하면 **그 컨트롤러에서 발생한 예외만** 처리한다.
  → `OtherController`에서 난 예외는 여기서 처리되지 않는다.

## 2. 사용자 정의 예외

```java
// 사용자정의 예외
public class MemberRegistException extends Exception {
    public MemberRegistException(String message) {
        super(message);   // 부모(Exception)에 예외 메시지 저장
    }
}
```

```java
@GetMapping("/controller-user")
public String userException() throws MemberRegistException {
    boolean check = true;
    if (check) {
        throw new MemberRegistException("당신은 우리서비스의 회원이 될 수 없습니다!");
    }
    return "/";
}

@ExceptionHandler(MemberRegistException.class)
public String memberRegistException(Model model, MemberRegistException exception) {
    model.addAttribute("exception", exception);
    return "error/memberRegist";
}
```

```html
<!-- error/memberRegist.html -->
<h3 th:text="${exception.message}"></h3>
```

- `throw` : 예외를 **직접 발생**시킨다.
- `Exception`을 상속한 예외(checked 예외)를 던지면 메소드에 `throws`를 반드시 붙여야 한다.
- 핸들러 메소드도 일반 핸들러처럼 `Model`을 받을 수 있다.
  HTML에서 `${exception.message}`를 쓰려면 **모든 핸들러에서** `model.addAttribute("exception", ...)`로 담아야 한다.

## 3. @ControllerAdvice - 전역 레벨

```java
/*
@ControllerAdvice
* - 모든 컨트롤러에 적용되는 공통 코드를 모아놓는 공간
* - 여러 컨트롤러에서 발생한 예외를 공통 처리할 수 있는 클래스
* */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public String nullPointerExceptionHandler(NullPointerException exception) {
        System.out.println("Global 레벨의 exception 처리!!");
        return "error/nullPointer";
    }

    @ExceptionHandler(MemberRegistException.class)
    public String MemberRegistExceptionHandler(Model model, MemberRegistException exception) {
        model.addAttribute("exception", exception);
        return "error/memberRegist";
    }
}
```

| | `@ExceptionHandler` | `@ControllerAdvice` |
|---|---|---|
| 붙는 곳 | **메소드** | **클래스** |
| 역할 | 이 예외가 나면 이렇게 처리 | 안에 있는 핸들러를 **모든 컨트롤러**에 적용 |

## 4. Exception 핸들러 - 마지막 안전망

```java
/*
 * Exception은 대부분의 일반 예외가 상속하는 타입
 * - 구체적인 처리 메소드가 없을 경우에는 마지막 안전망으로 사용
 * - Spring에서는 발생한 예외와 가장 구체적으로 일치하는 처리 메소드를 우선 사용
 * */
@ExceptionHandler(Exception.class)
public String exception(Exception exception) {
    return "error/default";
}

// 전역 ArrayIndexOutOfBoundsException
// - Exception 핸들러보다 구체적인 타입이므로 이 메소드가 우선 실행된다.
@ExceptionHandler(ArrayIndexOutOfBoundsException.class)
public String arrayIndexOutOfBoundsException(ArrayIndexOutOfBoundsException exception) {
    return "error/controller-array";
}
```

```java
// OtherController
@GetMapping("/other-controller-array")
public String arrayIndexOutOfBoundsException() {
    int[] arr = new int[1];
    System.out.println(arr[1]); // ArrayIndexOutOfBoundsException 발생 (0번만 있음)
    return "/";
}
```

- `ArrayIndexOutOfBoundsException`은 `Exception`의 하위 타입이지만, 더 구체적인 핸들러가 있으므로 그쪽이 실행된다.
- **같은 예외 타입을 처리하는 핸들러가 2개 있으면** 스프링이 어느 쪽을 쓸지 정하지 못해
  서버 시작 시 `Ambiguous @ExceptionHandler method mapped for ...` 에러가 난다.

## 우선순위 규칙

1. **같은 컨트롤러 안의 핸들러**가 `@ControllerAdvice`보다 먼저 실행된다.
   → `/controller-null`은 전역 핸들러가 있어도 "controller 레벨" 핸들러가 처리한다.
2. **더 구체적인 예외 타입**의 핸들러가 먼저 실행된다.
   → NPE는 `Exception` 핸들러가 아니라 `NullPointerException` 핸들러가 받는다.
3. 어디에도 맞는 핸들러가 없으면 스프링 기본 에러 페이지(Whitelabel, 500)가 나온다.

## 실습하면서 주의할 점

- **뷰 이름과 파일 이름은 대소문자까지 같아야 한다.**
  `return "error/NullPointer"` ↔ `nullPointer.html`은 Windows에서는 동작해도 jar/Linux에서는 찾지 못한다.
- **복사한 메소드는 예외 종류까지 바꿨는지 확인한다.**
  NPE를 던지는 코드를 그대로 두면 `MemberRegistException` 핸들러가 아니라 NPE 핸들러가 실행된다.
  메시지에 `[ArrayIndexOutOfBoundsException]`을 붙여도 실제로 던지는 예외가 바뀌지 않으면 해당 핸들러는 실행되지 않는다.
- **`@ExceptionHandler`에 같은 예외 타입을 두 번 지정하지 않는다.** (서버 시작 실패)
- 콘솔 메시지를 컨트롤러/전역 핸들러마다 다르게 두면 **어느 핸들러가 처리했는지** 구분하기 쉽다.
- 메소드 이름은 소문자로 시작한다. (`MemberRegistExceptionHandler` → `memberRegistExceptionHandler` 권장)
