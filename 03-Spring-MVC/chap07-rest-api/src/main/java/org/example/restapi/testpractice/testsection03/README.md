# Section 03 연습문제 - Validation & Enum 예외 처리

> 참고 코드: `section03/valid`, `section03/exceptionenum`
> 주제: **상품(Product)**
> 문제 번호는 코드 안의 `[문제 N]` 주석과 같아요.

---

## Part A. Validation (`valid` 패키지)

### [문제 1] ProductDTO 검증 규칙 달기
`ProductDTO.java`의 각 필드에 검증 어노테이션을 붙이세요.

| 필드 | 규칙 | 메시지 |
|---|---|---|
| `productCode` | 필수, `P-` + 숫자 4자리 (예: `P-0001`) | "상품코드는 P-숫자4자리 형식이어야 합니다." |
| `name` | null·빈 문자열·공백 불가, 2~20자 | "상품명은 반드시 입력해야 합니다." / "상품명은 2자 이상 20자 이하로 입력해야 합니다." |
| `price` | 필수, 100 이상 1,000,000 이하 | "가격은 필수입니다." / "가격은 100원 이상이어야 합니다." / "가격은 1,000,000원 이하여야 합니다." |
| `releaseDate` | 오늘 또는 과거 날짜만 | "출시일은 미래 날짜일 수 없습니다." |

> 힌트
> - 숫자 범위는 `@Min(값)`, `@Max(값)`을 써요. (`@Size`는 문자열 길이 전용)
> - 숫자 4자리 정규식: `[0-9]{4}`
> - 오늘 포함 과거: 원본 `UserDTO`의 주석을 보세요.

### [문제 2] 검증 실행하기
`ProductValidController.saveProduct()`의 매개변수에 검증이 실행되도록 어노테이션을 추가하세요.

### [문제 3] 검증 실패 응답 만들기
`ProductValidExceptionHandler`에 검증 실패 예외를 처리하는 메서드를 만드세요.
- 상태코드: **400**
- 응답 본문:
  ```json
  { "field": "price", "message": "가격은 100원 이상이어야 합니다." }
  ```

### [보너스 1] 실패한 필드 전부 보여주기
지금은 오류 1개만 보여줘요. `getFieldErrors()`(복수형)를 써서 **실패한 모든 필드**를 담아 응답해 보세요.
```json
{
  "price": "가격은 100원 이상이어야 합니다.",
  "name": "상품명은 2자 이상 20자 이하로 입력해야 합니다."
}
```

---

## Part B. Enum 예외 처리 (`exceptionenum` 패키지)

### [문제 4] ProductErrorCode 상수 추가
`PRODUCT_NOT_FOUND`를 참고해서 아래 상수 3개를 추가하세요.

| 상수명 | 상태코드 | 메시지 |
|---|---|---|
| `INVALID_PRODUCT_NO` | 400 | "상품 번호는 1 이상이어야 합니다." |
| `INVALID_QUANTITY` | 400 | "주문 수량은 1개 이상이어야 합니다." |
| `OUT_OF_STOCK` | 409 | "재고가 부족합니다." |

### [문제 5] ProductException 만들기
원본 `BusinessException`을 참고해서 만드세요.
- `RuntimeException`을 상속
- `ProductErrorCode`를 담는 필드 (읽기만 가능하게)
- 생성자 2개: `(ProductErrorCode)`, `(String message, ProductErrorCode)`
- getter

### [문제 6] ProductErrorResponse + ProductExceptionHandler 만들기
- `ProductErrorResponse`: `status`, `code`, `message`, `detail` 필드 + `from(ProductException)` 팩토리 메서드 + getter
- `ProductExceptionHandler`: `ProductException`을 받아서 `ErrorCode`의 상태코드와 `ProductErrorResponse`로 응답

### [문제 7] 컨트롤러에서 예외 던지기
`ProductExceptionController`의 `[문제 7-N]` 주석 위치에 알맞은 예외를 던지세요.

| 요청 | 기대 결과 |
|---|---|
| `GET /practice/enum-errors/products/0` | 400 `INVALID_PRODUCT_NO` |
| `GET /practice/enum-errors/products/99` | 404 `PRODUCT_NOT_FOUND`, detail: "상품번호 99에 해당하는 상품이 없습니다." |
| `POST /practice/enum-errors/products/1/order?quantity=0` | 400 `INVALID_QUANTITY` |
| `POST /practice/enum-errors/products/2/order?quantity=1` | 409 `OUT_OF_STOCK` (마우스 재고 0개) |
| `POST /practice/enum-errors/products/1/order?quantity=3` | 200 성공 |

> 문제를 풀기 전에 `/products/99`를 호출해 보세요. **500 에러**가 나요. 왜 그런지 생각해 보세요.
> (힌트: `Map.of()`에는 `null`을 넣을 수 없어요.)

---

## Part C. 코드 읽기 문제 (코드 작성 없음)

원본 `section03/exceptionenum/ErrorCode.java`를 보고 답하세요.

1. `INVALID_USER_NUMBER`의 메시지에 이상한 점이 있어요. 무엇일까요?
2. 주석에 `DUPLICATE_USER_NAME`이 `5xx : 서버 오류 응답`이라고 적혀 있어요. 실제 상태코드는 몇 번이고, 왜 5xx가 아닌 걸까요?
3. `ErrorResponse.from()`에서 `detail`이 `null`이 되는 경우는 언제일까요?

---

## 확인 방법
1. 서버 실행
2. `practice.http` 파일의 요청을 하나씩 실행
3. 위 표의 기대 결과와 같으면 성공
