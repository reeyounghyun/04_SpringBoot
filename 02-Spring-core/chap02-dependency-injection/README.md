# chap02-dependency-injection

스프링이 Bean끼리 필요한 객체를 연결해 주는 **의존성 주입(DI)** 실습.
같은 `BookService`를 **필드 → 세터 → 생성자** 세 가지 주입 방식으로 만들어 비교하고,
수정할 수 없는 외부 라이브러리 클래스를 `@Bean`으로 등록해 주입받는다.

- Spring Framework 7.0.8 (Java 17 이상 필요)

## 의존성 주입(DI)이란?

`BookService`가 일을 하려면 `BookDAO`가 필요하다(= `BookService`는 `BookDAO`에 **의존**한다).
이 `BookDAO`를 **내가 `new`로 만들어 넣는 대신, 스프링이 컨테이너에 있는 Bean을 찾아 넣어 주는 것**이 DI다.

```java
// 스프링 없이: 내가 직접 생성해서 연결
BookService bookService = new BookService(new BookDAOImpl());

// 스프링 사용: 컨테이너가 BookDAO Bean을 찾아 BookService에 넣어 둠
BookService bookService = context.getBean("bookServiceConstructor", BookService.class);
```

## 공통 흐름

```
① @ComponentScan("section01")      ← 패키지를 훑어 @Repository, @Service 클래스 발견
        ↓
② BookDAOImpl Bean 생성 ("bookDAO")
        ↓
③ BookService Bean 생성 + @Autowired 위치(필드/세터/생성자)로 bookDAO 주입
        ↓
④ context.getBean(...)으로 꺼내 사용
```

## 디렉토리 구조

```
src/main
├── java
│   ├── section01                           # 주입 방식 3가지 비교
│   │   ├── common
│   │   │   ├── BookDAO.java                # 도서 조회 인터페이스
│   │   │   ├── BookDAOImpl.java            # @Repository("bookDAO") - 예제 도서 2권 보관
│   │   │   └── BookDTO.java                # 도서 정보 DTO
│   │   ├── subsection01/field              # 필드 주입
│   │   ├── subsection02/setter             # 세터 주입
│   │   └── subsection03/constructor        # 생성자 주입 (권장)
│   └── section02                           # @Bean으로 외부 라이브러리 등록
│       ├── Application.java
│       ├── ContextConfiguration.java       # @Configuration + @ComponentScan + @Bean
│       └── common
│           ├── NotificationClient.java     # 알림 전송 인터페이스
│           ├── EmailNotificationClient.java  # 외부 라이브러리라고 가정 (수정 불가)
│           └── NotificationService.java    # @Service - NotificationClient를 생성자로 주입받음
└── resources
    └── xmlconfig/spirng-context.xml        # bookDAO Bean 정의 (현재 실습 코드에서는 사용하지 않음)
```

## section01 - 주입 방식 3가지

세 `BookService` 모두 하는 일은 같다(`selectAllBooks()`, `searchBookBySequence()`).
**`@Autowired`를 어디에 붙이느냐**만 다르고, 빈 이름으로 구분해서 꺼낸다.

### subsection01 - 필드 주입 (`bookServiceField`)

```java
@Autowired
private BookDAO bookDAO;
```

- 스프링이 `new BookService()`로 객체를 만든 **뒤**, 리플렉션으로 `private` 필드에 직접 넣는다.
- 코드는 가장 짧지만 `final`을 쓸 수 없어 의존관계의 불변성을 보장하기 어렵다.
- 스프링 없이 `new BookService()`로 테스트하면 넣어 줄 방법이 없어 `bookDAO`가 `null`이다.

### subsection02 - 세터 주입 (`bookServiceSetter`)

```java
private BookDAO bookDAO;

@Autowired
public void setBookDAO(BookDAO bookDAO) {
    this.bookDAO = bookDAO;
}
```

- 객체를 만든 **뒤** 스프링이 setter를 호출해서 넣는다.
- `final`을 쓸 수 없다 = setter로 의존 객체를 나중에 교체할 수 있다.
- 선택적이거나 바뀔 수 있는 의존성에 적합하다.

### subsection03 - 생성자 주입 (`bookServiceConstructor`) - 권장

```java
private final BookDAO bookDAO;

// @Autowired  ← 생성자가 하나면 생략 가능 (Spring 4.3+)
public BookService(BookDAO bookDAO) {
    this.bookDAO = bookDAO;
}
```

- 객체를 **만드는 순간** 생성자 인자로 받는다.
- 생성자 주입을 권장하는 이유
  1. 필수 의존성을 빠뜨린 채 객체를 생성할 수 없다.
  2. `final` 필드를 사용해 의존관계를 **불변**으로 유지할 수 있다.
- 테스트할 때도 `new BookService(가짜DAO)`처럼 직접 넣으면 된다.

### 주입 방식 비교

| | 필드 | 세터 | 생성자 |
|---|---|---|---|
| `@Autowired` 위치 | 필드 | setter 메서드 | 생성자 (1개면 생략 가능) |
| 주입 시점 | 객체 생성 후 | 객체 생성 후 | **객체 생성 시** |
| `final` 사용 | X | X | O |
| 의존 객체 교체 | 리플렉션으로만 | setter로 가능 | 불가 (불변) |
| null 위험 | 있음 | 있음 | 없음 |
| 스프링 없이 테스트 | 어려움 | 가능 | 쉬움 |

## final과 싱글톤은 다른 개념

| | `final` | 싱글톤 |
|---|---|---|
| 무엇이 한 번? | **필드에 값 대입**이 한 번 | **객체 생성**이 한 번 |
| 대상 | 객체 안의 필드 | 객체 자체 |
| 누가 보장? | 자바 컴파일러 | 스프링 컨테이너 |

- Bean은 기본이 싱글톤이라 여러 곳(여러 스레드)에서 **객체 하나를 공유**한다.
- 공유 객체의 필드가 바뀌면 위험하므로, 생성자 주입 + `final`로 **공유하되 바뀌지 않게** 만든다.
- `final`은 참조(가리키는 대상)를 못 바꾸게 할 뿐, 대상 객체 내부까지 얼리지는 않는다.

## section02 - @Bean으로 외부 라이브러리 등록

`EmailNotificationClient`는 **외부 라이브러리가 제공하는 클래스라 코드를 수정할 수 없다**고 가정한다.

- 코드를 못 고치니 `@Component`를 붙일 수 없다.
- 생성자에 `apiUrl`, `apiKey`, `timeoutSeconds` 같은 **설정값**이 필요해서 스프링이 혼자 만들 수도 없다.

그래서 내 설정 클래스에서 `@Bean` 메서드로 **직접 `new` 해서** 등록한다.

```java
@Configuration
@ComponentScan("section02")
public class ContextConfiguration {

    @Bean
    public NotificationClient notificationClient() {
        return new EmailNotificationClient("apiUrl", "api-key", 3);
    }
}
```

- 클래스(설계도)는 그대로 두고, 기존 생성자를 호출해 **내가 고른 설정값으로 객체를 만들어** 스프링에 맡기는 것이다.
- `NotificationService`는 `@Service`로 스캔되고, 생성자로 `notificationClient` Bean을 주입받는다.

### @Bean으로 할 수 있는 것 / 없는 것

| 할 수 있는 것 (만드는 방법 정하기) | 할 수 없는 것 |
|---|---|
| 생성자에 넣을 설정값 정하기 | `send()` 같은 클래스 내부 로직 변경 |
| 만든 뒤 setter 등 설정 메서드 호출 | 없는 메서드 추가 |
| 어떤 구현체를 쓸지 고르기 (반환 타입이 인터페이스) | |
| 이름을 다르게 해서 같은 클래스로 Bean 여러 개 만들기 | |

동작까지 바꾸고 싶으면 같은 인터페이스를 구현하는 클래스로 **감싸서(데코레이터)** 그 클래스를 Bean으로 등록한다.

### 실행 결과

```
contextConfiguration
notificationService      ← @Service (컴포넌트 스캔)
notificationClient       ← @Bean
메일 전송 요청: receiver=hello@gmail.com, message=가입 환영!, apiUrl=apiUrl, timeout=3초, 인증키 설정=true
```

## Bean 등록 방법 정리

| | 붙이는 곳 | 언제 쓰나 |
|---|---|---|
| `@Component` / `@Service` / `@Repository` | **내가 만든 클래스** | 코드를 수정할 수 있고 스프링이 알아서 만들 수 있을 때 |
| `@Bean` | **설정 클래스의 메서드** | 외부 라이브러리 클래스, 설정값으로 초기화가 필요한 객체 |
