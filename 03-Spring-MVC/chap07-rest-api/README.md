# chap07-rest-api

Spring MVC REST API 실습 프로젝트입니다.
`@RestController`로 **반환 타입에 따라 응답이 어떻게 달라지는지** 확인하고, `ResponseEntity`로 HTTP 응답 전체를 직접 구성합니다.

## 디렉토리 구조

```
src/main/java/org/example/restapi/
├── Chap07RestApiApplication.java
├── section01/response/
│   ├── ResponseRestController.java   반환 타입별 응답 예제
│   ├── Message.java                  응답용 객체 (httpStatusCode, message)
│   └── get.http                      요청 테스트 파일
└── section02/responseentity/
    └── ResponseEntityController.java ResponseEntity 예제 (진행 중)

src/main/resources/static/cat1.jpg    이미지 응답용 파일
```

## section01 - 반환 타입별 응답

기본 주소: `/response`

| 요청 주소 | 반환 타입 | 응답 결과 | Content-Type |
|---|---|---|---|
| `/hello` | `String` | `Hello World!` | text/plain |
| `/random` | `int` | `1` ~ `10` 중 랜덤 숫자 | application/json |
| `/message` | `Message` | `{"httpStatusCode":200,"message":"메시지를 응답합니다."}` | application/json |
| `/list` | `List<String>` | `["사과","바나나","귤"]` | application/json |
| `/map` | `Map<Integer, String>` | `{"200":"정상응답","404":"페이지를 찾을 수 없습니다","500":"개발자 잘못"}` | application/json |
| `/image` | `byte[]` | 고양이 이미지 (`cat1.jpg`) | image/jpeg |
| `/entity` | `ResponseEntity<Message>` | `{"httpStatusCode":200,"message":"응답 성공"}` | application/json |

### @ResponseBody / @RestController

- `@ResponseBody` : 반환값이 View를 거치지 않고 **응답 본문(Body)에 직접** 쓰여진다.
- `@Controller` + `@ResponseBody` = `@RestController`

### JSON 변환 규칙

JSON(JavaScript Object Notation)은 자바스크립트가 인지할 수 있는 객체 형태의 문자열입니다.
Spring 내부의 **Jackson** 라이브러리가 반환값을 JSON으로 자동 변환합니다.

| JSON | 괄호 | Java 타입 | 예시 |
|---|---|---|---|
| 배열 | `[ ]` | `List`, 배열 | `["사과", "바나나", "귤"]` |
| 객체 | `{ }` | `Map`, 일반 클래스 | `{"httpStatusCode": 200, "message": "..."}` |

- 일반 클래스는 **getter**를 기준으로 JSON key가 만들어진다. → `Message`에 getter가 꼭 필요
- `Map`의 key는 JSON에서 항상 문자열이 된다. (`200` → `"200"`)
- `HashMap`(`Collectors.toMap`)은 순서를 보장하지 않아서 응답 순서가 섞일 수 있다.
- 서로 중첩 가능 : `List<Message>` → `[{...}, {...}]`

### 이미지 응답 (byte[])

```java
@GetMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
public byte[] getImage() throws IOException {
    return getClass().getResourceAsStream("/static/cat1.jpg").readAllBytes();
}
```

- `produces`를 지정해야 `Content-Type: image/jpeg`로 전송되어 이미지로 표시된다.
- `static` 폴더의 파일은 `http://localhost:8080/cat1.jpg`로도 바로 접근 가능하다.

### ResponseEntity

HTTP 응답의 **상태코드 + 헤더 + 바디**를 직접 구성할 때 사용하는 클래스입니다.

```java
return ResponseEntity.ok(new Message(200, "응답 성공"));                // 상태 200 + 바디
return ResponseEntity.status(404).body(new Message(404, "응답 성공"));  // 상태 404 + 바디
```

> `status()`로 정한 **실제 HTTP 상태코드**와 `body()`에 담은 **메시지 내용**은 서로 별개입니다.
> Spring은 바디 내용을 검사하지 않기 때문에 404인데 "응답 성공"이 나갈 수도 있습니다. 둘을 맞추는 건 개발자의 몫입니다.

## HTTP 상태코드 참고

| 범위 | 의미 | 예시 |
|---|---|---|
| 2xx | 성공 | 200 OK |
| 4xx | 클라이언트 잘못 | 400 잘못된 요청, 404 페이지 없음 |
| 5xx | 서버 잘못 | 500 서버 내부 오류 (처리 안 된 예외, DB 장애 등) |

## 실행 / 테스트 방법

1. `Chap07RestApiApplication` 실행 (기본 포트 8080)
2. `section01/response/get.http`를 열고 각 요청 옆 ▶ 버튼 클릭
3. 또는 브라우저에서 `http://localhost:8080/response/...` 접속
