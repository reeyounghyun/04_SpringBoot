package org.example.jpaintro.repository;


import org.example.jpaintro.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * [Repository 계층] DB 접근 담당
 *
 * JpaRepository<T, ID>
 * - T  : 관리할 엔티티 타입 (Menu)
 * - ID : 엔티티 식별자(PK) 타입 (Integer) → 제네릭이라 int 대신 Integer 사용
 *
 * [상속 구조]
 * Repository (마커 인터페이스, 메서드 없음)
 *   ├─ CrudRepository             : save(), findById(), findAll(), deleteById(), existsById(), count() ...
 *   ├─ PagingAndSortingRepository : findAll(Pageable), findAll(Sort)
 *   └─ ⭐ JpaRepository            : 위 기능 + flush(), saveAndFlush(), deleteAllInBatch(), getReferenceById()
 *
 * [자동 구현 원리]
 * - 구현 클래스를 만들지 않아도, 스프링이 실행될 때 이 인터페이스를 스캔해서
 *   JpaRepositoryFactory 가 "프록시 객체"를 만들어 빈으로 등록한다.
 * - 실제 CRUD 로직은 SimpleJpaRepository 가 수행한다.
 * - 아래처럼 직접 선언한 메서드는 QueryLookupStrategy 가 "메서드 이름"을 분석해 JPQL 을 만들어준다.
 */
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    // [쿼리 메서드] 이름으로 부분 검색
    // → where menu_name like '%keyword%'
    List<Menu> findByMenuNameContaining(String keyword);

    // [쿼리 메서드] 특정 가격보다 비싼 메뉴가 하나라도 있는지 여부
    // → select ... where menu_price > ? (존재하면 true)
    boolean existsByMenuPriceGreaterThan(int menuPrice);

    // [쿼리 메서드] 이름으로 삭제
    // → 조회(select) 후 한 건씩 delete 실행
    // ⚠️ 직접 선언한 삭제 메서드에는 트랜잭션이 자동으로 걸리지 않으므로
    //    호출하는 Service 메서드에 @Transactional 을 붙여야 한다.
    void deleteByMenuName(String menuName);
}
