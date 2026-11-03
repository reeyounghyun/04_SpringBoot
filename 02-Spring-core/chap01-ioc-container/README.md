# chap01-ioc-container

스프링 IoC 컨테이너(ApplicationContext)에 Bean을 등록하고 꺼내 쓰는 실습.
같은 일을 **XML → Java(@Bean) → 어노테이션(@ComponentScan)** 세 가지 설정 방식으로 해보고, Bean의 기본 scope(singleton)를 확인한다.

## IoC(제어의 역전)란?

객체를 **내가 `new`로 만드는 대신, 스프링 컨테이너가 만들어 보관하고** 필요할 때 꺼내 쓰는 것.

```java
// 스프링 없이: 내가 직접 생성
MemberDTO member = new MemberDTO(1, "user01", "pass01", "bear");

// 스프링 사용: 컨테이너가 만들어 둔 객체를 꺼내기만 함
ApplicationContext context = new AnnotationConfigApplicationContext(ContextConfiguration.class);
MemberDTO member = context.getBean("member", MemberDTO.class);
```

## 공통 흐름

```
① 설정 정보 작성 (XML / @Bean / @Component)
        ↓
② ApplicationContext 생성  ← 설정을 읽고 객체를 미리 만들어 보관
        ↓
③ context.getBean(...)     ← 만들어진 객체를 꺼내 사용
```

## 디렉토리 구조

```
src/main
├── java
│   ├── common
│   │   ├── MemberDTO.java          # 회원 정보 DTO (어노테이션 없음)
│   │   ├── MemberDAO.java          # @Repository - 컴포넌트 스캔 대상
│   │   └── TestDTO.java            # @Component  - 컴포넌트 스캔 대상
│   ├── section01/xmlconfig         # XML 설정
│   ├── section02/javaconfig        # Java 설정 (@Configuration + @Bean)
│   ├── section03/annotationconfig  # 어노테이션 설정 (@ComponentScan)
│   └── section04                   # Bean scope (singleton)
└── resources
    └── xmlconfig/spirng-context.xml  # section01용 Bean 정의
```

## 섹션별 정리

### section01 - XML 설정

- `spirng-context.xml`에 `<bean id="member" class="common.MemberDTO">`로 Bean 정의
- `<constructor-arg>`로 생성자 인자 전달
- `GenericXmlApplicationContext`로 컨테이너 생성

### section02 - Java 설정

- `@Configuration` 클래스가 XML 파일 역할
- `@Bean(name = "member")` 메서드가 반환한 객체가 Bean으로 등록됨
- `AnnotationConfigApplicationContext`로 컨테이너 생성
- 주로 **외부 라이브러리 객체**나 **설정값으로 초기화가 필요한 객체**를 등록할 때 사용

### section03 - 어노테이션 설정

- `@ComponentScan(basePackages = "common")`이 패키지를 훑어 `@Component` 계열 클래스를 자동 등록
  - `TestDTO`(@Component) → `testDTO`
  - `MemberDAO`(@Repository) → `memberDAO`
  - `MemberDTO`는 어노테이션이 없어 등록되지 않음
- `context.getBeanDefinitionNames()`로 등록된 Bean 이름 목록 확인

### section04 - Bean scope (singleton)

```java
new MemberDTO(...) == new MemberDTO(...)                        // false : 객체 2개
context.getBean(MemberDTO.class) == context.getBean(MemberDTO.class) // true  : 같은 객체 1개
```

- Bean의 기본 scope는 **singleton** - 컨테이너가 한 번만 만들고 계속 재사용

## 설정 방식 비교

| | 설정 위치 | 컨테이너 구현체 | Bean 등록 방식 |
|---|---|---|---|
| section01 | `spirng-context.xml` | `GenericXmlApplicationContext` | `<bean>` 태그 |
| section02 | `@Configuration` 클래스 | `AnnotationConfigApplicationContext` | `@Bean` 메서드 |
| section03 | `@Configuration` + `@ComponentScan` | `AnnotationConfigApplicationContext` | 클래스의 `@Component` (자동) |

## getBean 조회 방법

| 방법 | 코드 | 특징 |
|---|---|---|
| 이름 | `getBean("member")` | Object 반환 → 형변환 필요 |
| 타입 | `getBean(MemberDTO.class)` | 같은 타입 Bean이 2개 이상이면 예외 |
| 이름 + 타입 | `getBean("member", MemberDTO.class)` | 가장 명확 |
