# Section 04 연습문제 - HATEOAS

> 참고 코드: `section04` (`DtoModelAssembler`, `HateoasTestController`)
> 주제: **도서(Book)**
> 문제 번호는 코드 안의 `[문제 N]` 주석과 같아요.

---

### [문제 1] BookModelAssembler 완성하기
`BookModelAssembler.toModel()`에서 `BookDTO`를 `EntityModel`로 감싸고 링크 2개를 붙이세요.

| rel(링크 이름) | 가리키는 메서드 | 예시 URL |
|---|---|---|
| `self` | `findBookByNo(dto.getNo())` | `/practice/hateoas/books/1` |
| `books` | `findAllBooks()` | `/practice/hateoas/books` |

> 힌트: `linkTo(methodOn(컨트롤러.class).메서드(인자)).withSelfRel()` / `.withRel("이름")`

### [문제 2] 전체 조회에 링크 붙이기
`findAllBooks()`는 지금 `books`를 그대로 응답에 담고 있어요.
`stream()` + `assembler`를 써서 `List<EntityModel<BookDTO>>`로 바꾼 뒤 담으세요.

### [문제 3] 없는 도서는 404
`findBookByNo()`에서 도서를 못 찾으면 **404 Not Found**를 응답하세요.

> 힌트: `ResponseEntity.notFound().build()`

### [문제 4] 단건 조회에도 링크 붙이기
`findBookByNo()`도 찾은 도서를 `assembler.toModel()`로 감싸서 응답하세요.

기대 응답 (`GET /practice/hateoas/books/1`). `links` 모양은 원본 `/hateoas/users` 응답과 같으면 돼요.
```json
{
  "httpStatus": 200,
  "message": "조회 성공",
  "result": {
    "book": {
      "no": 1,
      "title": "자바의 정석",
      "author": "남궁성",
      "price": 30000,
      "links": [
        { "rel": "self",  "href": "http://localhost:8080/practice/hateoas/books/1" },
        { "rel": "books", "href": "http://localhost:8080/practice/hateoas/books" }
      ]
    }
  }
}
```

### [보너스 1] next 링크
`toModel()`에서 **다음 번호의 도서가 있을 때만** `next` 링크(`/books/{no + 1}`)를 추가하세요.
- 1번, 2번 도서에는 `next`가 있고, 마지막 3번 도서에는 없어야 해요.
- 힌트: `EntityModel.of(dto)`로 먼저 만들고, 조건에 따라 `model.add(링크)`를 해요.
- 힌트: Assembler에서 "다음 도서가 있는지" 알려면 도서 목록이 필요해요. 컨트롤러에 `existsBook(int no)` 같은 메서드를 만들어도 되고, 마지막 번호를 Assembler에 넘겨줘도 돼요. 방법은 자유예요.

### [보너스 2] 저자별 조회 + 링크
`GET /practice/hateoas/books/search?author=남궁성` API를 만들고, 각 도서에 `self` 링크를 붙여 응답하세요.

---

## 코드 읽기 문제
1. `BookModelAssembler`에 `@Component`가 없으면 무슨 일이 생길까요?
2. 원본 `HateoasTestController.findUserByUseNo()`는 없는 번호(예: 99)로 조회하면 어떤 응답을 줄까요? 그게 왜 문제일까요?
3. `withSelfRel()`과 `withRel("users")`의 차이는 무엇일까요?

---

## 확인 방법
1. 서버 실행
2. `practice.http`의 요청 실행
3. 응답에 `links`가 위 예시처럼 나오면 성공
