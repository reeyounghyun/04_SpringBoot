# chap00-jpa-intro

Spring Data JPA 입문 프로젝트 — `tbl_menu` 테이블을 대상으로 메뉴 조회 / 등록 / 수정 API를 만든다.

## 실행 전 준비 : `application.yaml` 직접 만들기

`src/main/resources/application.yaml` 은 **DB 비밀번호가 들어 있어서 Git에 올리지 않는다.** (`.gitignore` 처리됨)
그래서 저장소를 새로 받았다면 이 파일이 없고, **그대로 실행하면 아래 오류로 앱이 뜨지 않는다.**

```
Failed to configure a DataSource: 'url' attribute is not specified ...
```

### 1. 파일 생성

`src/main/resources/application.yaml` 을 만들고 아래 내용을 붙여넣는다.

```yaml
spring:
  application:
    name: chap00-jpa-intro
  datasource:
    url: jdbc:mysql://localhost:3306/menudb
    username: <DB 계정>
    password: <DB 비밀번호>
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      # ddl-auto 옵션
      # 1. none        : 스키마(테이블 구조)를 변경하지 않음
      # 2. validate    : 엔티티와 테이블 구조가 일치하는지 검사 (다르면 실행 실패)
      # 3. update      : 엔티티 변경사항을 기존 테이블에 반영 (기존 테이블/데이터는 유지)
      # 4. create      : 기존 테이블을 삭제하고 엔티티대로 다시 생성 (데이터 삭제됨)
      # 5. create-drop : 실행할 때 생성하고, 종료할 때 삭제
      ddl-auto: none
    properties:
      hibernate:
        format_sql: true            # SQL 을 줄바꿈해서 보기 좋게 출력
logging:
  level:
    org.hibernate.SQL: debug              # 실행되는 SQL 출력
    org.hibernate.orm.jdbc.bind: trace    # ? 자리에 바인딩된 실제 값 출력
```

### 2. 값 수정

- `<DB 계정>`, `<DB 비밀번호>` 를 **본인 MySQL 접속 정보**로 바꾼다.
- `menudb` 데이터베이스와 `tbl_menu` 테이블이 MySQL 에 미리 만들어져 있어야 한다. (`ddl-auto: none` 이라 JPA 가 테이블을 만들지 않음)

> ⚠️ 이 파일은 `.gitignore` 에 등록되어 있으므로 `git add .` 를 해도 커밋되지 않는다. 비밀번호를 지우지 않아도 안전하다.

## 실행 및 확인

1. `Chap00JpaIntroApplication` 실행
2. Swagger 접속 : http://localhost:8080/swagger-ui/index.html
3. API 테스트

| 메서드 | URL | 설명 |
|---|---|---|
| GET | `/api/menus` | 메뉴 전체 조회 |
| POST | `/api/menus` | 메뉴 등록 |
| PUT | `/api/menus/{menuCode}` | 메뉴 수정 (없는 번호면 404) |

등록 요청 예시 (`menuCode` 는 DB 가 자동 생성하므로 보내지 않는다)

```json
{
  "menuName": "김치찌개",
  "menuPrice": 8000,
  "categoryCode": 4,
  "orderableStatus": "Y"
}
```

## 패키지 구조

```
org.example.jpaintro
├── config       # SwaggerConfig : Swagger(OpenAPI) 문서 설정
├── controller   # MenuController : HTTP 요청을 받는 입구
├── service      # MenuService : 비즈니스 로직, Entity ↔ DTO 변환
├── repository   # MenuRepository : JpaRepository 상속, DB 접근
├── entity       # Menu : tbl_menu 테이블과 매핑되는 엔티티
└── dto          # MenuRequestDTO(요청) / MenuResponseDTO(응답)
```

흐름 : `클라이언트 → Controller → Service → Repository → DB`

## 이 프로젝트에서 한 것

### 1. 프로젝트 셋팅
- Spring Boot 4.1.2 / Java 21 / Gradle
- 의존성 : Spring Data JPA, Spring Web MVC, MySQL Connector, Lombok, springdoc(Swagger)
- 계층별 패키지 구성 : `controller` / `service` / `repository` / `entity` / `dto` / `config`

### 2. Entity — `Menu`
- `@Entity`, `@Table(name = "tbl_menu")` 로 테이블과 매핑
- `@Id` + `@GeneratedValue(strategy = GenerationType.IDENTITY)` → PK 를 DB 의 AUTO_INCREMENT 에 맡김
- `@Column(name = "menu_name")` 로 **DB 컬럼(snake_case) ↔ 자바 필드(camelCase)** 연결

  | DB 컬럼 | 자바 필드 |
  |---|---|
  | menu_code | menuCode |
  | menu_name | menuName |
  | menu_price | menuPrice |
  | category_code | categoryCode |
  | orderable_status | orderableStatus |

- `@NoArgsConstructor(access = AccessLevel.PROTECTED)` → JPA 용 기본 생성자는 두되, 외부에서 함부로 `new Menu()` 못 하게 막음
- 등록용 생성자는 `menuCode` 를 받지 않음 (DB 가 자동 생성)
- `change()` 메서드로 값 변경 (생성자와 수정이 같은 로직 재사용)

### 3. Repository — `MenuRepository`
- `JpaRepository<Menu, Integer>` 상속 (`<엔티티 타입, PK 타입>`)
- 인터페이스만 선언하면 `save()`, `findAll()`, `findById()` 등을 SQL 없이 사용 가능

### 4. DTO — 요청 / 응답 분리
- `MenuRequestDTO` : 클라이언트 → 서버. **`menuCode` 없음** (DB 가 번호를 매기므로)
- `MenuResponseDTO` : 서버 → 클라이언트. **`menuCode` 포함** (몇 번으로 저장됐는지 알려줌)
- `MenuResponseDTO.from(Menu menu)` : Entity → DTO 변환 **정적 팩토리 메서드**
- Entity 를 직접 주고받지 않는 이유 : 바뀌면 안 되는 값(PK) 보호, DB 구조 노출 방지

### 5. Service — `MenuService`
- `@Service` + `@RequiredArgsConstructor` → `final` 필드 생성자 주입
- **findAll** : `findAll()` 로 `List<Menu>` 조회 → `stream().map(MenuResponseDTO::from)` 으로 DTO 변환
- **create** : RequestDTO 값으로 `new Menu(...)` → `save()` → 저장된 엔티티(번호 채워짐)를 DTO 로 반환
- **update** :
  - `findById(menuCode).orElseThrow(...)` → 없으면 `ResponseStatusException(HttpStatus.NOT_FOUND)` 로 **404** 응답
  - `menu.change(...)` 로 값만 바꾸고 `save()` 를 호출하지 않음
  - → `@Transactional` 이 끝날 때 JPA 가 바뀐 값을 감지해 자동으로 UPDATE (**변경 감지, Dirty Checking**)

### 6. Controller — `MenuController`
- `@RestController` + `@RequestMapping("/api/menus")`
- `@GetMapping`, `@PostMapping`, `@PutMapping("/{menuCode}")`
- `@RequestBody` 로 JSON → `MenuRequestDTO`, `@PathVariable` 로 URL 의 메뉴 번호 받기

### 7. 설정
- `ddl-auto` 옵션 5가지 비교 → 이미 만들어진 `menudb` 를 쓰므로 `none`
  - `ddl-auto` 는 **테이블 구조(스키마)** 만 다루고, 데이터(행)는 건드리지 않음 (단, `create` / `create-drop` 은 테이블을 지우므로 데이터도 사라짐)
- SQL 로그 : `org.hibernate.SQL: debug` + `format_sql: true` → 실행된 SQL 확인, `org.hibernate.orm.jdbc.bind: trace` → `?` 에 들어간 값 확인
- Swagger : `SwaggerConfig` 에서 `OpenAPI` 빈으로 문서 제목/설명/버전 설정
- DB 비밀번호가 담긴 `application.yaml` 은 `.gitignore` 로 저장소에서 제외
