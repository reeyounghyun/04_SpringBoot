# chap02-mapping

순수 JPA(Hibernate)로 **자바 클래스(엔티티)와 DB 테이블을 어떻게 연결(매핑)하는지** 실습하는 프로젝트다.
`Member`(회원) 엔티티 하나를 섹션마다 조금씩 바꿔 가며 아래 세 가지를 확인한다.

| 섹션 | 주제 | 핵심 어노테이션 |
|---|---|---|
| section01 | 엔티티 기본 매핑, 기본키 자동 생성 | `@Entity` `@Table` `@Id` `@Column` `@GeneratedValue` |
| section02 | Enum 타입을 DB에 저장하는 방법 | `@Enumerated` |
| section03 | JPA가 엔티티 값에 접근하는 방식 | `@Access` |

> Spring Boot 없이 `persistence.xml` 과 `EntityManagerFactory` 로 JPA를 직접 실행한다.
> 그래서 Spring이 대신 해 주던 일(설정 읽기, EntityManager 생성·종료, 트랜잭션 관리)을 코드로 직접 작성한다.
> 모든 코드는 `src/test` 아래에 있고 JUnit 테스트로 실행한다.

---

## 1. 기술 스택

| 구분 | 내용 |
|---|---|
| ORM | Hibernate `7.4.5.Final` (Jakarta Persistence 3.2 구현체) |
| DB | MySQL, `menudb` 데이터베이스 (`mysql-connector-j 9.7.0`) |
| 라이브러리 | Lombok `1.18.48` — getter/setter/생성자 자동 생성 |
| 테스트 | JUnit `6.0.0` |

### build.gradle 에서 주의할 점

```gradle
compileOnly 'org.projectlombok:lombok:1.18.48'
annotationProcessor 'org.projectlombok:lombok:1.18.48'
testCompileOnly 'org.projectlombok:lombok:1.18.48'
testAnnotationProcessor 'org.projectlombok:lombok:1.18.48'
```

- 이 프로젝트는 Spring Boot 플러그인이 없어서 **버전을 꼭 적어야** 한다. (Spring Boot는 버전을 대신 관리해 주지만 여기선 안 됨)
- 코드가 `src/test` 에 있으므로 `testCompileOnly` / `testAnnotationProcessor` 도 필요하다.
  이게 없으면 `@Getter` `@Setter` 가 동작하지 않아 `setMemberId()` 같은 메서드에 빨간 줄이 생긴다.

---

## 2. 실행 전 준비

1. MySQL 서버를 켜고 `menudb` 데이터베이스를 만들어 둔다.
2. `src/test/resources/META-INF/persistence.xml` 의 접속 정보를 **본인 MySQL 계정**으로 바꾼다.
   ```xml
   <property name="jakarta.persistence.jdbc.user" value="계정"/>
   <property name="jakarta.persistence.jdbc.password" value="비밀번호"/>
   ```
3. 테이블은 직접 만들지 않아도 된다. `hibernate.hbm2ddl.auto=create` 설정 때문에 테스트를 실행하면 Hibernate가 엔티티를 보고 테이블을 만든다.

---

## 3. JPA 동작 흐름 이해하기

### 3-1. persistence.xml → EntityManagerFactory → EntityManager

```
persistence.xml (설정 묶음, 이름: jpatest)
      │  Persistence.createEntityManagerFactory("jpatest")
      ▼
EntityManagerFactory  ── 애플리케이션 전체에서 1개. 만들 때 DB 연결 설정을 읽고, 이 시점에 테이블 생성(DDL)도 실행된다.
      │  entityManagerFactory.createEntityManager()
      ▼
EntityManager         ── 요청(테스트)마다 1개. persist / find 등으로 엔티티를 저장·조회한다.
      │
      ▼
영속성 컨텍스트        ── EntityManager 안의 "엔티티 보관소". persist 한 엔티티가 여기에 먼저 저장된다.
```

- `EntityManagerFactory` 는 만드는 비용이 커서 **한 번만** 만들고 공유한다.
- `EntityManager` 는 스레드 간 공유하면 안 되므로 **테스트마다 새로** 만들고 닫는다.

### 3-2. 테스트 클래스의 공통 틀

모든 테스트 클래스(`Demo`, `PropertyAccessTest` 등)는 같은 틀을 가진다.

```java
@BeforeAll   // 테스트 클래스 시작 시 1번 → 팩토리 생성 (+ 테이블 생성)
public static void initFactory() {
    entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
}

@BeforeEach  // 각 @Test 실행 전 → EntityManager 생성
public void initManager() {
    entityManager = entityManagerFactory.createEntityManager();
}

@AfterEach   // 각 @Test 실행 후 → EntityManager 종료
public void closeManager() { entityManager.close(); }

@AfterAll    // 모든 테스트 종료 후 1번 → 팩토리 종료
public static void closeFactory() { entityManagerFactory.close(); }
```

실행 순서 : `@BeforeAll` → (`@BeforeEach` → `@Test` → `@AfterEach`) × 테스트 개수 → `@AfterAll`

### 3-3. persist 만으로는 DB에 저장되지 않는다

```java
EntityTransaction entityTransaction = entityManager.getTransaction();
entityTransaction.begin();     // 트랜잭션 시작
entityManager.persist(member); // 영속성 컨텍스트에 저장 (아직 DB에 INSERT 안 됨)
entityTransaction.commit();    // 이 시점에 INSERT SQL이 DB로 전송되고 확정된다
```

- `persist()` 는 엔티티를 **영속성 컨텍스트에 넣을 뿐**이다. 실제 `INSERT` 는 `commit()` 할 때 실행된다.
- 그래서 `commit()` 이 없는 테스트(`PropertyAccessTest`, `FieldAccessTest`)는
  `find()` 로 조회는 되지만(영속성 컨텍스트에서 꺼내 옴) **DB 테이블에는 데이터가 남지 않는다.**
- 반면 테이블 자체는 만들어진다. 테이블 생성(DDL)은 `EntityManagerFactory` 를 만들 때 실행되고 바로 확정되기 때문에 롤백되지 않는다.

---

## 4. persistence.xml 설정

```xml
<persistence-unit name="jpatest">
    <class>com.section01.entity.Member</class>
    <class>com.section02.enumType.Member</class>
    <class>com.section03.field.Member</class>
    <properties>
        <!-- DB 접속 정보 -->
        <property name="jakarta.persistence.jdbc.driver" value="com.mysql.cj.jdbc.Driver"/>
        <property name="jakarta.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/menudb"/>
        ...
        <!-- SQL 출력 -->
        <property name="hibernate.show_sql" value="true"/>
        <property name="hibernate.format_sql" value="true"/>
        <property name="hibernate.highlight_sql" value="true"/>
        <!-- 테이블 자동 생성 -->
        <property name="hibernate.hbm2ddl.auto" value="create"/>
    </properties>
</persistence-unit>
```

| 설정 | 설명 |
|---|---|
| `persistence-unit name` | 설정 묶음의 이름. `createEntityManagerFactory("jpatest")` 의 인자와 같아야 한다. |
| `<class>` | JPA가 관리할 엔티티 등록. **빠지면** `persist` 할 때 `UnknownEntityTypeException` 발생 |
| `show_sql` | 실행되는 SQL을 콘솔에 출력 |
| `format_sql` | SQL을 줄바꿈해서 보기 좋게 출력 |
| `highlight_sql` | SQL 키워드에 색을 입혀 출력 |
| `hbm2ddl.auto` | 엔티티를 기준으로 테이블을 자동 생성/삭제 |

- 모든 테스트는 `jpatest` 를 사용한다. `jpabook` 은 같은 내용의 예비 설정이다.
- 새 섹션에서 엔티티를 만들면 **사용하는 persistence-unit 에 `<class>` 를 추가**해야 한다.

### hbm2ddl.auto 옵션

| 값 | 동작 | 사용 시기 |
|---|---|---|
| `none` | 아무것도 하지 않음 (default) | 운영 환경 |
| `create` | 실행할 때마다 기존 테이블 drop 후 새로 생성 | 학습·테스트 (현재 설정) |
| `create-drop` | `create` 와 같지만, 애플리케이션 종료 시 drop | 테스트 후 흔적을 남기지 않을 때 |
| `update` | 기존 테이블 유지, 변경된 부분(컬럼 추가)만 반영 — 컬럼 삭제·타입 변경은 반영 안 됨 | 개발 중 데이터를 유지하고 싶을 때 |
| `validate` | DDL 실행 없이 엔티티와 테이블 구조가 맞는지 검증만 | 운영 환경 점검 |

> ⚠️ **`create` 는 실행할 때마다 테이블을 DROP 한다.**
> `jpatest` 에 등록된 엔티티 3개의 테이블은 **어느 테스트를 실행하든 전부** 지워지고 새로 만들어진다.
> 예) `EnumTypeMappingTest` 로 데이터를 넣은 뒤 `FieldAccessTest` 를 실행하면 `tbl_member_section02` 도 비워진다.
> 기존 데이터가 있는 테이블(예: `tbl_menu`)을 `@Table` 에 지정하면 그 데이터도 사라지니 주의한다.

---

## 5. 프로젝트 구조

```
src/test
├─ java/com
│  ├─ practice
│  │   └─ Demo.java                  // EntityManager 생명주기 기본 틀 (복사해서 쓰는 템플릿)
│  ├─ section01/entity               // 엔티티 기본 매핑 + 식별자 생성 전략
│  │   ├─ Member.java
│  │   ├─ PropertyAccessTest.java
│  │   └─ SequenceTableTest.java
│  ├─ section02/enumType             // Enum 타입 매핑
│  │   ├─ RoleType.java
│  │   ├─ Member.java
│  │   └─ EnumTypeMappingTest.java
│  └─ section03/field                // 필드 접근 방식 (@Access)
│      ├─ Member.java
│      └─ FieldAccessTest.java
└─ resources/META-INF
   └─ persistence.xml
```

### 섹션마다 엔티티 이름·테이블 이름을 다르게 하는 이유

섹션마다 `Member` 클래스를 따로 두는데, 패키지가 달라도 **엔티티 이름과 테이블 이름은 프로젝트 전체에서 겹치면 안 된다.**

| 섹션 | 엔티티 이름 (`@Entity(name=)`) | 테이블 (`@Table(name=)`) |
|---|---|---|
| section01 | `member_section01` | `tbl_member_section01` |
| section02 | `member_section02` | `tbl_member_section02` |
| section03 | `member_section03` | `tbl_member_section03` |

- 엔티티 이름이 겹치면 → `EntityManagerFactory` 생성 단계에서 `DuplicateMappingException` 으로 실패
- 테이블 이름이 겹치면 → 구조가 다른 두 엔티티가 한 테이블을 서로 덮어씀

---

## 6. section01 — 엔티티 기본 매핑 / 식별자 생성 전략

### 6-1. 엔티티 기본 매핑

**엔티티(Entity)** 는 DB 테이블과 매핑되는 자바 클래스다. 객체 1개 = 테이블의 행 1개, 필드 1개 = 컬럼 1개로 대응된다.

```java
@Entity(name = "member_section01")       // 이 클래스는 JPA가 관리하는 엔티티 (이름 지정)
@Table(name = "tbl_member_section01")    // 매핑될 테이블 이름
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Member {

    @Id                                  // 기본키(PK)
    @Column(name = "member_no")          // 매핑될 컬럼 이름
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int memberNo;

    @Column(name = "member_id")
    private String memberId;
    ...
}
```

| 어노테이션 | 역할 |
|---|---|
| `@Entity` | JPA가 관리하는 클래스라고 표시. `name` 은 JPQL 등에서 쓰는 엔티티 이름 (생략 시 클래스 이름) |
| `@Table` | 매핑할 테이블 이름 지정 (생략 시 엔티티 이름을 사용) |
| `@Id` | 기본키 필드 지정. 엔티티에는 반드시 하나 있어야 한다 |
| `@Column` | 필드를 어떤 컬럼에 매핑할지 지정 (생략 시 필드 이름을 사용) |

- JPA는 리플렉션으로 객체를 만들기 때문에 **기본 생성자가 반드시 필요**하다. → `@NoArgsConstructor`

### 6-2. 식별자(기본키) 자동 생성 — `@GeneratedValue`

기본키 값을 개발자가 직접 넣지 않고, JPA나 DB가 자동으로 만들게 하는 설정이다.

| 전략 | 동작 방식 | 주로 쓰는 DB |
|---|---|---|
| `IDENTITY` | DB에 생성을 맡긴다. `INSERT` 할 때 DB가 1, 2, 3… 자동 증가 값을 부여 | MySQL (`AUTO_INCREMENT`) |
| `SEQUENCE` | DB의 **시퀀스 객체**에서 다음 번호를 받아 온다 | Oracle, PostgreSQL |
| `TABLE` | 키 생성 **전용 테이블**을 만들어 다음 번호를 기록·관리한다. 모든 DB에서 사용 가능 | DB 종류에 상관없이 |
| `AUTO` | DB 종류를 보고 JPA가 알맞은 전략을 자동 선택 (default) | — |

- section01 은 `TABLE` 전략을 사용한다. 테스트를 실행하면 회원 테이블 외에 **번호를 세는 테이블**(Hibernate 기본값 `hibernate_sequences`)이 추가로 만들어진다.
- `IDENTITY` 는 DB가 `INSERT` 해야 번호가 생기므로, `persist()` 하는 즉시 `INSERT` 가 실행된다는 특징이 있다.

### 6-3. 테스트

| 테스트 | 하는 일 |
|---|---|
| `PropertyAccessTest.테이블_만들기_테스트()` | 회원 1명 `persist` 후 `find` 로 조회해서 `memberId` 비교. 트랜잭션이 없어서 **테이블만 생기고 데이터는 남지 않는다** (3-3 참고) |
| `SequenceTableTest.식별자_매핑_테스트()` | `memberNo` 를 **넣지 않고** 회원 2명을 저장 → `commit()` → DB 에서 번호가 자동으로 매겨졌는지 확인 |

> ⚠️ `@GeneratedValue` 를 쓰면서 `setMemberNo(1)` 처럼 기본키를 직접 넣으면,
> Hibernate가 "이미 저장된 객체"로 판단해 `persist` 에서 `detached entity passed to persist` 오류가 날 수 있다.

---

## 7. section02 — Enum 타입 매핑

### 7-1. 왜 필요한가

회원 권한처럼 **정해진 값 중 하나**만 가져야 하는 데이터는 `String` 보다 `enum` 으로 두는 게 안전하다.
(`"ROLE_MEMBR"` 같은 오타를 컴파일 단계에서 막을 수 있다)

```java
public enum RoleType {
    ADMIN, MEMBER
}
```

하지만 DB 에는 enum 타입이 없으므로, **어떤 값으로 바꿔서 저장할지** `@Enumerated` 로 정해 줘야 한다.

```java
@Column(name = "member_role")
// @Enumerated(EnumType.ORDINAL) // enum에 선언된 '순서'가 저장됨 (ADMIN=0 / MEMBER=1)
@Enumerated(EnumType.STRING)     // enum 이름이 문자열로 저장됨 ("ADMIN" / "MEMBER")
private RoleType memberRole;
```

### 7-2. ORDINAL vs STRING

| 설정 | DB 에 저장되는 값 | 컬럼 타입 | 장단점 |
|---|---|---|---|
| `EnumType.ORDINAL` (default) | 선언 순서 숫자 `0`, `1` | 정수 | 저장 공간이 작다. 하지만 **enum 순서가 바뀌면 기존 데이터의 의미가 바뀐다** |
| `EnumType.STRING` ✅ | 이름 문자열 `"ADMIN"`, `"MEMBER"` | 문자열 | 공간을 조금 더 쓰지만 순서가 바뀌어도 안전 — **실무 권장, 현재 적용** |

ORDINAL 이 위험한 예 :

```java
// 처음:   ADMIN(0), MEMBER(1)            → MEMBER 회원은 DB에 1 로 저장됨
// 나중에: GUEST(0), ADMIN(1), MEMBER(2)  → 맨 앞에 GUEST 추가
// 결과:   DB에 저장된 1 이 이제 ADMIN 으로 읽힌다 → 일반 회원이 관리자가 됨!
```

### 7-3. 테스트

`EnumTypeMappingTest.enum타입_매핑_테스트()`

1. `member.setMemberRole(RoleType.MEMBER)` 로 enum 값을 넣고 `persist`
2. `find` 로 다시 조회해서 `memberNo` 비교, `toString()` 출력
3. `commit()` → DB 의 `tbl_member_section02.member_role` 컬럼 확인
   - `STRING` 이면 `MEMBER`, `ORDINAL` 이면 `1` 이 저장된다.

---

## 8. section03 — 필드 접근 방식 (`@Access`)

### 8-1. 접근 방식이란

JPA가 엔티티의 값을 **읽고 쓸 때 어디를 거칠지** 정하는 것이다.

| 방식 | JPA가 값에 접근하는 경로 | 지정 방법 |
|---|---|---|
| 필드 접근 `AccessType.FIELD` | 필드에 **직접** 접근 (getter/setter 를 거치지 않음) | `@Id` 를 필드에 붙이거나 `@Access(AccessType.FIELD)` |
| 프로퍼티 접근 `AccessType.PROPERTY` | **getter/setter 메서드**를 통해 접근 | `@Id` 를 getter 에 붙이거나 `@Access(AccessType.PROPERTY)` |

- 기본값은 **`@Id` 가 어디에 붙어 있는지**로 결정된다. 필드에 있으면 필드 접근, getter 에 있으면 프로퍼티 접근.
- `@Access` 를 **클래스 위**에 붙이면 엔티티 전체의 접근 방식을, **필드·메서드 위**에 붙이면 그 항목만 따로 지정한다.

### 8-2. 코드

```java
@Entity(name = "member_section03")
@Table(name = "tbl_member_section03")
@Setter
@Access(AccessType.FIELD) // JPA 가 엔티티의 값을 어디를 통해 쓸지 → 필드에 직접
@NoArgsConstructor @AllArgsConstructor @ToString
public class Member {
    ...
    // 확인용 커스텀 getter : 값 앞에 문구를 붙여서 반환
    public String getMemberId() {
        return "getter에서 동작함..." + memberId;
    }
}
```

- `@Getter` 를 빼고 getter 를 직접 만들어, 반환값에 `"getter에서 동작함..."` 을 붙였다.
- **필드 접근**이므로 JPA는 이 getter 를 쓰지 않고 `memberId` 필드 값을 그대로 DB 에 저장한다.
  → DB 에는 `user01` 이 저장된다.
- 만약 **프로퍼티 접근**으로 바꾸면 JPA가 getter 를 호출하므로 `getter에서 동작함...user01` 이 저장된다.
  이 차이를 확인하는 것이 이 섹션의 목적이다.
- 단, `@Access(AccessType.PROPERTY)` 한 줄만 바꾸면 안 된다. `@Id` · `@Column` 을 getter 위로 옮기고, 모든 컬럼에 getter 가 있어야 한다. (그대로 두면 매핑 오류가 날 수 있다)

### 8-3. 테스트

`FieldAccessTest.필드_접근_테스트()`

1. 회원을 만들어 `persist`
2. `find(Member.class, 1)` 로 조회 → 같은 객체인지 `assertEquals` 로 비교, `toString()` 출력

> 트랜잭션 `commit()` 이 없어서 현재는 DB 에 데이터가 남지 않는다.
> DB 에 저장된 값으로 필드/프로퍼티 접근 차이를 확인하려면 `begin()` / `commit()` 을 추가한다.

---

## 9. 테스트 실행 방법

IntelliJ 에서 테스트 클래스나 메서드 옆의 ▶ 버튼으로 실행한다.

### 자주 만나는 문제

| 증상 | 원인 | 해결 |
|---|---|---|
| `ClassNotFoundException` / `Unable to load class` | 프로젝트 경로에 **한글**(`C:\Users\이영현\...`)이 있어 Gradle 테스트 실행기가 클래스를 못 찾음 | `Settings → Build, Execution, Deployment → Build Tools → Gradle → Run tests using: IntelliJ IDEA` |
| `UnknownEntityTypeException` | 사용하는 persistence-unit 에 엔티티가 `<class>` 로 등록되지 않음 | `persistence.xml` 에 `<class>패키지.클래스</class>` 추가 |
| `DuplicateMappingException` | 서로 다른 클래스의 `@Entity(name=)` 이 같음 | 섹션별로 엔티티 이름을 다르게 지정 |
| `setXxx()` 등에 빨간 줄 | Lombok 이 테스트 코드에 적용되지 않음 | `build.gradle` 에 `testCompileOnly` / `testAnnotationProcessor` 추가 후 Gradle 새로고침 |
| `@Table` / `@Column` 이름에 빨간 줄 (컴파일은 됨) | IntelliJ 가 연결된 DB 에서 그 테이블·컬럼을 못 찾음 (아직 테이블이 없음) | 테스트를 한 번 실행해 테이블을 만든 뒤 Database 창 새로고침 🔄 |
| 테스트는 성공했는데 테이블이 비어 있음 | `commit()` 이 없거나, 다른 테스트 실행 시 `create` 로 테이블이 다시 만들어짐 | `begin()`/`commit()` 확인, 해당 테스트만 실행 후 바로 새로고침 🔄 |
