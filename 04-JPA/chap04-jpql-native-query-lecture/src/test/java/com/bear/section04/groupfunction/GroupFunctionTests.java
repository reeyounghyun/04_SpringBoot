package com.bear.section04.groupfunction;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// 그룹함수
public class GroupFunctionTests {
	
	private static EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	
	@BeforeAll
	public static void initFactory() {
		entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
	}
	
	@BeforeEach
	public void initManager() {
		entityManager = entityManagerFactory.createEntityManager();
	}
	
	@AfterAll
	public static void closeFactory() {
		entityManagerFactory.close();
	}
	
	@AfterEach
	public void closeManager() {
		entityManager.close();
	}

	/*
	* JPQL의 그룹함수 COUNT, MAX, MIN, SUM, AVG .. 같은 그룹함수도 사용 할 수 있다.
	*
	* 주의할점
	* 1. 그룹함수의 반환 타입은 결과값이 정수면 Long, 실수면 Double   (수정 전: 상수면 Dobule)
	* 2. 값이 없는 상태에서 그룹함수는 null 응답 ( COUNT 만 0을 응답 한다.)
	*  주의할 점 -> 기본자료형을 사용하면 NullPointerException(NPE)이 발생하여 래핑 클래스로 사용하는게 좋다.   (수정 전: 널포인트입셉션)
	* */
	@Test
	public void 특정_카테고리의_등록된_메뉴_수_조회() {
		
		// given
		// 숫자 3을 categoryCodeParameter에 넣는다.
		int categoryCodeParameter = 3;
		
		// when
		// jpql을 테이블에서 가져올 값은 메뉴가격, menu_section04 테이블에서, categoryCode가 3인 메뉴의 개수를 문자열로 가져온다.
		String jpql = "SELECT COUNT(m.menuPrice) FROM menu_section04 m WHERE m.categoryCode = :categoryCode";

		// jpql을 실행하고, categoryCodeParameter를 넣는다.
		long countOfMenu = entityManager.createQuery(jpql, Long.class)
				.setParameter("categoryCode", categoryCodeParameter)
				.getSingleResult();
		
		// then
		// countOfMenu가 0보다 크거나 같으면 true를 반환한다.
		assertTrue(countOfMenu >= 0);
		System.out.println("countOfMenu = " + countOfMenu);
		
	}
	
	@Test
	public void count를_제외한_다른_그룹함수의_조회결과가_없는_경우_테스트() {
		
		// given
		int categoryCodeParameter = 3;

		// when
		// SUM으로 메뉴 가격의 합계를 구하는 JPQL문 (menu_section04 엔티티에서 categoryCode가 :categoryCode(=3)인 메뉴 대상)
		// 조회 결과가 없으면 SUM은 null을 반환한다.
		String jpql = "SELECT SUM(m.menuPrice) FROM menu_section04 m WHERE m.categoryCode = :categoryCode";


		// 정의: assertThrows 원하는 예외를 발생시키는지 확인하는 테스트를 할때 사용하는 검증, 지정한 예외가 발생하면 테스트 통과.
		// 문법:  assertThrows(원하는 예외 클레스, () => {예외 발생코드})
		// then
		assertThrows(NullPointerException.class, () -> {
			// 기본자료형
			// null을 long에 넣으려고 자동 언박싱(null.longValue())이 일어나서 NPE 발생
			long sumOfPrice = entityManager.createQuery(jpql, Long.class)
					.setParameter("categoryCode", categoryCodeParameter)
					.getSingleResult();
					// 예외가 발생 하는가?
		});

		/*
		* 반환값을 담는 변수를 Wrapping 클래스로 선언해 null 값이 변환되어도 NPE가 발생하지 않음
		*  - Wrapper 클래스(Long)는 null을 담을 수 있기 때문에   (수정 전: wrappingsms null 값을 지정하지 않기 떄문에)
		*
		*
		* */
		// 랩핑 자료형 =->  NULL값을 받을수 있기때문에 해결할 수 있다.
		assertDoesNotThrow(() -> {
			// Long은 객체라서 null을 그대로 담을 수 있어요. 예외 없음
			Long sumOfPrice = entityManager.createQuery(jpql, Long.class)
					.setParameter("categoryCode", categoryCodeParameter)
					.getSingleResult();
			// 예외를 방생 시키지 않는다.

		});
	}
	
	@Test
	public void group_by절과_having절을_사용한_조회_테스트() {
		
		//given
		long minPrice = 50000L;
		
		//when
		String jpql = "SELECT m.categoryCode, SUM(m.menuPrice) " +
						"FROM menu_section04 m " +
						"GROUP BY m.categoryCode " +
						"HAVING SUM(m.menuPrice) >= :minPrice";
		
		List<Object[]> sumPriceOfCategoryList = entityManager.createQuery(jpql, Object[].class)
				.setParameter("minPrice", minPrice)  // minPrice은 선업이 long 타입이기 떄문에 같은 타입으로 선언 해줘야 한다.
				.getResultList();
		
		//then
		assertNotNull(sumPriceOfCategoryList);
		sumPriceOfCategoryList.forEach(row -> {
			Arrays.stream(row).forEach(System.out::println);
		});
	}
}
