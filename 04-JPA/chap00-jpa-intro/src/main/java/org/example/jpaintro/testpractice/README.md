# chap00 연습문제 - Spring Data JPA 카테고리 CRUD

> 참고 코드: `org.example.jpaintro` 의 `entity/Menu`, `repository/MenuRepository`, `dto/*`, `service/MenuService`, `controller/MenuController`
> 주제: **카테고리(Category)** — `menudb.tbl_category`
> 문제 번호는 코드 안의 `[문제 N]` 주석과 같아요.

---

## 시작 전에

### 대상 테이블 `tbl_category`

| 컬럼 | 타입 | 설명 |
|---|---|---|
| `category_code` | INT, PK, AUTO_INCREMENT | 카테고리 번호 |
| `category_name` | VARCHAR(30), NOT NULL | 카테고리 이름 |
| `ref_category_code` | INT, NULL 허용 | 상위 카테고리 번호 (최상위면 NULL) |

- `ref_category_code` 가 같은 테이블의 `category_code` 를 가리키는 **계층 구조**예요. (예: 1 `식사` ← 4 `한식`, 5 `중식` ...)
- `ddl-auto: none` 이라 JPA 가 테이블을 만들지 않아요. MySQL 에서 `SELECT * FROM tbl_category;` 로 미리 데이터를 확인해 두세요.

### 진행 방법
1. 스켈레톤 상태에서도 **앱은 정상 실행**돼요. (기존 메뉴 API 영향 없음)
2. 아직 안 푼 기능을 호출하면 `UnsupportedOperationException` → **500** 이 나요.
3. 문제를 풀 때마다 `practice.http` 로 확인하세요. 콘솔의 **SQL 로그**도 꼭 같이 보세요.

### 만들 API

| 메서드 | URL | 설명 | 관련 문제 |
|---|---|---|---|
| GET | `/api/categories` | 전체 조회 | 4 |
| GET | `/api/categories/{categoryCode}` | 단건 조회 (없으면 404) | 5 |
| POST | `/api/categories` | 등록 | 6 |
| PUT | `/api/categories/{categoryCode}` | 수정 (없으면 404) | 7 |
| DELETE | `/api/categories/{categoryCode}` | 삭제 (없으면 404) | 8 |
| GET | `/api/categories/search?keyword=식` | 이름 검색 | 2-2, 9-1 |
| GET | `/api/categories/{refCategoryCode}/children` | 하위 카테고리 조회 | 2-3, 9-2 |
| GET | `/api/categories/roots` | 최상위 카테고리 조회 | 2-4, 9-3 |

---

## Part A. Entity (`entity/Category.java`)

### [문제 1] Category 엔티티 매핑 완성하기

| 번호 | 할 일 |
|---|---|
| 1-1 | `tbl_category` 테이블과 매핑 |
| 1-2 | 기본 생성자를 **PROTECTED** 로 생성 (Lombok) |
| 1-3 | PK 를 DB AUTO_INCREMENT 에 맡기기 |
| 1-4 | 세 필드에 컬럼명 연결 (`category_code`, `category_name`, `ref_category_code`) |
| 1-5 | 등록용 생성자 `Category(String categoryName, Integer refCategoryCode)` — 내부에서 `change()` 재사용 |
| 1-6 | `change(String categoryName, Integer refCategoryCode)` 메서드 |

> 힌트
> - 1-1 을 안 하면 Hibernate 는 클래스 이름대로 `category` 테이블을 찾아요. → 조회 시 `Table 'menudb.category' doesn't exist` 오류. 직접 겪어 보세요!
> - 1-5 생성자만 만들고 1-2 를 빼먹으면? → 자바는 기본 생성자를 자동으로 안 만들어 줘요. JPA 는 기본 생성자가 **꼭** 필요해요.

---

## Part B. Repository (`repository/CategoryRepository.java`)

### [문제 2-1] (개념) 제네릭 의미
`JpaRepository<Category, Integer>` 에서 `Category` 와 `Integer` 는 각각 무엇인가요?

### [문제 2-2 ~ 2-4] 쿼리 메서드 만들기
SQL 을 쓰지 않고 **메서드 이름만으로** 조회 쿼리를 만들어 보세요.

| 번호 | 원하는 SQL | 반환 타입 |
|---|---|---|
| 2-2 | `WHERE category_name LIKE '%keyword%'` | `List<Category>` |
| 2-3 | `WHERE ref_category_code = ?` | `List<Category>` |
| 2-4 | `WHERE ref_category_code IS NULL` | `List<Category>` |

> 힌트
> - 규칙: `findBy` + **필드명(자바 필드, 컬럼명 아님!)** + 조건 키워드
> - 조건 키워드: `Containing`, `IsNull`, `StartsWith`, `GreaterThan` ...
> - 메서드 이름에 오타가 있으면 **앱 실행 자체가 실패**해요. (`No property 'xxx' found for type 'Category'`)

---

## Part C. DTO (`dto/CategoryResponseDTO.java`)

### [문제 3] `from()` 정적 팩토리 메서드
`Category` → `CategoryResponseDTO` 변환을 완성하세요. (`MenuResponseDTO.from()` 참고)

> `CategoryRequestDTO` 는 완성본이에요. 왜 RequestDTO 에는 `categoryCode` 가 없을까요?

---

## Part D. Service (`service/CategoryService.java`)

### [문제 4] 전체 조회
`findAll()` → `stream().map(CategoryResponseDTO::from).toList()`

### [문제 5] 단건 조회
- `findById()` 의 반환 타입은 `Optional<Category>` 예요.
- 없으면 `ResponseStatusException(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다.")`

### [문제 6] 등록
RequestDTO → `new Category(...)` → `save()` → 반환된 엔티티로 DTO 생성.
응답에 `categoryCode` 가 채워져 오는지 확인하세요.

### [문제 7] 수정 — 변경 감지(Dirty Checking)
- 조회 후 `change()` 만 호출하고 **`save()` 는 호출하지 마세요.**
- 콘솔에 `UPDATE` SQL 이 찍히는지 확인하세요.
- 🤔 **실험**: `@Transactional` 을 지우고 다시 요청해 보세요. 응답은 바뀐 값인데 DB 는? 왜 그럴까요?

### [문제 8] 삭제
- 없는 번호면 404. (`existsById()` 또는 `findById()` 활용)
- 삭제는 `deleteById()` 또는 `delete(entity)`
- ⚠️ 이미 메뉴가 연결된 카테고리(예: 4 `한식`)는 FK 때문에 삭제가 실패해요. **[문제 6] 으로 새로 만든 카테고리**로 테스트하세요.

### [문제 9] 쿼리 메서드 사용
[문제 2-2 ~ 2-4] 에서 만든 메서드로 `searchByName`, `findChildren`, `findRoots` 를 완성하세요.

> 💡 조회 메서드에는 `@Transactional(readOnly = true)` 를 붙여 보세요. 변경 감지를 안 해서 조금 더 가벼워요.

---

## Part E. Controller (`controller/CategoryController.java`)

### [문제 10] 매핑 어노테이션 붙이기
메서드 본문은 완성돼 있어요. **어노테이션만** 붙이세요.
- `@GetMapping` / `@PostMapping` / `@PutMapping` / `@DeleteMapping`
- `@PathVariable` / `@RequestBody` / `@RequestParam`

> ⚠️ `/roots`, `/search` 와 `/{categoryCode}` 가 둘 다 GET 이에요. 스프링은 **고정 경로를 변수 경로보다 우선** 매칭하니 충돌하지 않아요.

---

## 보너스

### [보너스 1] 201 Created 응답
등록 API 가 `200` 대신 **`201 Created`** 를 반환하도록 바꿔 보세요. (`ResponseEntity.status(HttpStatus.CREATED).body(...)`)
삭제 API 는 **`204 No Content`** 로 바꿔 보세요.

### [보너스 2] 상위 카테고리 검증
등록/수정 시 `refCategoryCode` 가 null 이 아닌데 **존재하지 않는 번호**면 `400 Bad Request` + `"상위 카테고리가 존재하지 않습니다."`

### [보너스 3] 자기 자신을 상위로?
수정 시 `refCategoryCode` 가 **자기 자신의 categoryCode** 와 같으면 `400` 으로 막아 보세요.

### [보너스 4] 개념 질문
1. `save()` 를 호출하지 않았는데 UPDATE 가 실행되는 이유는?
2. Entity 를 컨트롤러에서 직접 반환하지 않고 DTO 로 바꾸는 이유 두 가지는?
3. `@NoArgsConstructor(access = AccessLevel.PROTECTED)` 에서 `private` 이 아니라 `protected` 인 이유는?

---

## 정답

> 먼저 직접 풀어 보고 펼쳐 보세요!

<details>
<summary>[문제 1] Category</summary>

```java
@Entity
@Table(name = "tbl_category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_code")
    private Integer categoryCode;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "ref_category_code")
    private Integer refCategoryCode;

    public Category(String categoryName, Integer refCategoryCode) {
        change(categoryName, refCategoryCode);
    }

    public void change(String categoryName, Integer refCategoryCode) {
        this.categoryName = categoryName;
        this.refCategoryCode = refCategoryCode;
    }
}
```
import 추가: `lombok.AccessLevel`, `lombok.NoArgsConstructor`
</details>

<details>
<summary>[문제 2] CategoryRepository</summary>

- 2-1: `Category` = 다룰 **엔티티 타입**, `Integer` = 그 엔티티의 **PK(@Id) 타입**

```java
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findByCategoryNameContaining(String keyword);   // 2-2

    List<Category> findByRefCategoryCode(Integer refCategoryCode); // 2-3

    List<Category> findByRefCategoryCodeIsNull();                  // 2-4
}
```
</details>

<details>
<summary>[문제 3] from()</summary>

```java
public static CategoryResponseDTO from(Category category) {
    return new CategoryResponseDTO(
            category.getCategoryCode(),
            category.getCategoryName(),
            category.getRefCategoryCode()
    );
}
```
- RequestDTO 에 `categoryCode` 가 없는 이유: PK 는 DB 가 자동 생성하고, 클라이언트가 바꾸면 안 되는 값이라서.
</details>

<details>
<summary>[문제 4 ~ 9] CategoryService</summary>

```java
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // [문제 4]
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findAll() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponseDTO::from)
                .toList();
    }

    // [문제 5]
    @Transactional(readOnly = true)
    public CategoryResponseDTO findById(Integer categoryCode) {
        return CategoryResponseDTO.from(getCategory(categoryCode));
    }

    // [문제 6]
    @Transactional
    public CategoryResponseDTO create(CategoryRequestDTO request) {
        Category category = new Category(request.getCategoryName(), request.getRefCategoryCode());
        Category saved = categoryRepository.save(category);
        return CategoryResponseDTO.from(saved);
    }

    // [문제 7] save() 없음 → 트랜잭션 커밋 시 변경 감지로 UPDATE
    @Transactional
    public CategoryResponseDTO update(Integer categoryCode, CategoryRequestDTO request) {
        Category category = getCategory(categoryCode);
        category.change(request.getCategoryName(), request.getRefCategoryCode());
        return CategoryResponseDTO.from(category);
    }

    // [문제 8]
    @Transactional
    public void delete(Integer categoryCode) {
        Category category = getCategory(categoryCode);
        categoryRepository.delete(category);
    }

    // [문제 9-1]
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> searchByName(String keyword) {
        return categoryRepository.findByCategoryNameContaining(keyword).stream()
                .map(CategoryResponseDTO::from)
                .toList();
    }

    // [문제 9-2]
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findChildren(Integer refCategoryCode) {
        return categoryRepository.findByRefCategoryCode(refCategoryCode).stream()
                .map(CategoryResponseDTO::from)
                .toList();
    }

    // [문제 9-3]
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findRoots() {
        return categoryRepository.findByRefCategoryCodeIsNull().stream()
                .map(CategoryResponseDTO::from)
                .toList();
    }

    // 공통 : 없으면 404 (5, 7, 8 에서 재사용)
    private Category getCategory(Integer categoryCode) {
        return categoryRepository.findById(categoryCode)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."));
    }
}
```
import 추가: `Category`, `HttpStatus`, `Transactional`, `ResponseStatusException`

- [문제 7] 실험 답: `@Transactional` 이 없으면 `findById()` 가 끝나는 순간 트랜잭션·영속성 컨텍스트가 닫혀 엔티티가 **준영속** 상태가 돼요. 자바 객체 값은 바뀌었으니 응답은 바뀐 값이지만, 변경 감지가 일어나지 않아 **DB 는 그대로**예요.
</details>

<details>
<summary>[문제 10] CategoryController</summary>

```java
@GetMapping
public List<CategoryResponseDTO> findAll() { ... }

@GetMapping("/{categoryCode}")
public CategoryResponseDTO findById(@PathVariable Integer categoryCode) { ... }

@PostMapping
public CategoryResponseDTO create(@RequestBody CategoryRequestDTO request) { ... }

@PutMapping("/{categoryCode}")
public CategoryResponseDTO update(@PathVariable Integer categoryCode,
                                  @RequestBody CategoryRequestDTO request) { ... }

@DeleteMapping("/{categoryCode}")
public void delete(@PathVariable Integer categoryCode) { ... }

@GetMapping("/search")
public List<CategoryResponseDTO> search(@RequestParam String keyword) { ... }

@GetMapping("/{refCategoryCode}/children")
public List<CategoryResponseDTO> findChildren(@PathVariable Integer refCategoryCode) { ... }

@GetMapping("/roots")
public List<CategoryResponseDTO> findRoots() { ... }
```
</details>

<details>
<summary>[보너스 1 ~ 3]</summary>

```java
// 보너스 1 - Controller
@PostMapping
public ResponseEntity<CategoryResponseDTO> create(@RequestBody CategoryRequestDTO request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(request));
}

@DeleteMapping("/{categoryCode}")
public ResponseEntity<Void> delete(@PathVariable Integer categoryCode) {
    categoryService.delete(categoryCode);
    return ResponseEntity.noContent().build();
}
```

```java
// 보너스 2 - Service (create / update 첫 줄에서 호출)
private void validateParent(Integer refCategoryCode) {
    if (refCategoryCode != null && !categoryRepository.existsById(refCategoryCode)) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "상위 카테고리가 존재하지 않습니다.");
    }
}

// 보너스 3 - Service update 안에서
if (categoryCode.equals(request.getRefCategoryCode())) {
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "자기 자신을 상위 카테고리로 지정할 수 없습니다.");
}
```
</details>

<details>
<summary>[보너스 4] 개념 질문</summary>

1. `@Transactional` 안에서 조회한 엔티티는 **영속성 컨텍스트**가 관리해요. JPA 는 조회 시점의 스냅샷을 보관해 두고, 커밋(flush) 시점에 현재 값과 비교해 바뀐 필드가 있으면 UPDATE 를 자동 실행해요. (**변경 감지, Dirty Checking**)
2. ① PK 처럼 클라이언트가 바꾸면 안 되는 값을 보호 ② DB 구조(엔티티)를 API 에 노출하지 않아, 엔티티가 바뀌어도 API 스펙이 흔들리지 않음 (+ 지연 로딩 프록시 직렬화 문제 방지)
3. JPA(Hibernate)가 **프록시 객체**를 만들 때 엔티티를 상속하는데, `private` 이면 하위 클래스에서 생성자를 호출할 수 없어요. 그래서 외부 사용은 막으면서 상속은 허용하는 `protected` 를 써요.
</details>
