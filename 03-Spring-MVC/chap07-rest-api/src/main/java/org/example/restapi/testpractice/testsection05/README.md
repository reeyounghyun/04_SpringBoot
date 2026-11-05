# Section 05 연습문제 - Swagger(springdoc) API 문서화

> 참고 코드: `section05/swagger` (`SwaggerTestController`, `SwaggerTestControllerImpl`)
> 주제: **회원(Member)**
> 문제 번호는 코드 안의 `[문제 N]` 주석과 같아요.

`MemberSwaggerControllerImpl`에는 CRUD 동작 코드가 이미 다 있어요.
여러분은 **인터페이스(`MemberSwaggerController`)에 문서 설명을 다는 것**과 **예외 상황 처리**를 해요.

Swagger 화면: http://localhost:8080/swagger-ui.html

---

## Part A. 문서화

### [문제 1] 컨트롤러 그룹 이름 달기
`MemberSwaggerController` 인터페이스에 `@Tag`를 붙이세요.
- name: `"회원 관리 API (연습)"`
- description: `"Section #5 연습문제 - 회원 CRUD"`

> 문제를 풀기 전에 Swagger 화면을 열어보세요. 이름이 `member-swagger-controller-impl`처럼 보일 거예요.

### [문제 2] 메서드 5개 문서화
각 메서드에 `@Operation`, `@ApiResponses`, (필요하면) `@Parameter`를 붙이세요.

| 메서드 | summary | 응답 코드 | @Parameter |
|---|---|---|---|
| `findAllMembers` | 회원 전체 조회 | 200 조회 성공 | - |
| `findMemberByNo` | 회원 상세 조회 | 200 조회 성공 / 404 회원 없음 | `memberNo` "조회할 회원 번호", 필수 |
| `registMember` | 회원 등록 | 201 등록 성공 / 400 잘못된 요청 | - |
| `modifyMember` | 회원 정보 수정 | 200 수정 성공 / 404 회원 없음 | `memberNo` "수정할 회원 번호", 필수 |
| `removeMember` | 회원 삭제 | 204 삭제 성공 / 404 회원 없음 | `memberNo` "삭제할 회원 번호", 필수 |

- 200 응답은 `MemberResponseMessage` 스키마를 보여주세요.
- 본문이 없는 응답(201, 204, 404 등)은 `@Schema(hidden = true)`로 숨기세요.

### [문제 3] @Override 붙이기
`MemberSwaggerControllerImpl`의 5개 메서드 위에 `@Override`를 붙이세요.
- 붙인 다음, 인터페이스의 메서드 이름 하나를 일부러 바꿔 보세요. 어떤 오류가 나나요?

### [보너스 1] DTO 필드 설명 달기
`MemberDTO` 필드에 `@Schema(description = "...", example = "...")`를 붙이세요.
Swagger 화면 아래쪽 **Schemas**에서 `MemberDTO`를 펼쳐 설명과 예시가 보이면 성공이에요.

```java
@Schema(description = "회원 아이디", example = "member01")
private String id;
```

---

## Part B. 예외 상황 처리

### [문제 4] 없는 회원이면 404
지금 없는 번호(예: 99)를 넣으면 이렇게 돼요.

| 메서드 | 지금 결과 | 고친 후 |
|---|---|---|
| `findMemberByNo` | 200 + `"member": null` | 404 |
| `modifyMember` | 500 (NullPointerException) | 404 |
| `removeMember` | 204 (지운 게 없는데 성공) | 404 |

`findMember()`가 `null`을 돌려줄 때 **404 Not Found**를 응답하도록 고치세요.

### [문제 5] Location 헤더 확인
`registMember`는 201과 함께 `Location` 헤더를 돌려줘요.
등록 후 응답 헤더의 `Location` 값으로 GET 요청을 보내서, 방금 등록한 회원이 조회되는지 확인하세요.

---

## Part C. 코드 읽기 문제
원본 `section05/swagger/SwaggerTestControllerImpl.java`를 보고 답하세요.

1. `registUser()`의 `Location` 주소는 `"/entity/users/" + newUserNo`예요. 이 주소로 요청하면 회원이 조회될까요? 올바른 주소는 무엇일까요?
2. `PutEntity()`는 `@PathVariable int userNo`를 받지만 실제로 사용하지 않아요. 대신 무엇으로 회원을 찾고 있나요? REST 관점에서 어떤 점이 어색할까요?
3. `PutEntity`라는 메서드 이름은 Java 이름 규칙에 맞을까요?
4. `users.get(users.size() - 1).getNo()`는 회원 목록이 **비어 있을 때** 어떻게 될까요?

---

## 확인 방법
1. 서버 실행 → http://localhost:8080/swagger-ui.html
2. "회원 관리 API (연습)" 그룹에 5개 API 설명이 나오는지 확인
3. Swagger 화면에서 **Try it out**으로 직접 요청해 보거나 `practice.http` 실행
