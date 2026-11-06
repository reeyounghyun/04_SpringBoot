package com.bear.section06.subquery;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SubQueryTests {
	
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
*
* 조건절(WHERE)에 쓰는 서브 쿼리 -> 중첩 서브쿼리   (수정 전: 같이 테이블)
* FROM절에 쓰는 서브쿼리 -> 인라인 뷰   (수정 전: Form절, 파싱 테이블)
*
* */

	@Test
	public void 서브쿼리를_이용한_메뉴_조회_테스트() {

		// given
		String categoryNameParameter = "한식"; // 한식파라미터
		/*
		 서브쿼리는 쿼리안에 쿼리가 있는걸 뜻함.
		* */
		// when
		String jpql = "SELECT m " +
						"FROM menu_section06 m " +
						"WHERE m.categoryCode = (SELECT c.categoryCode " +
											"FROM category_section06 c " +
													"WHERE c.categoryName = :categoryName)";
		// 한식 카테고리를 조회하여 한식 메뉴를 조회한다.

		List<Menu> menuList = entityManager.createQuery(jpql, Menu.class)
				.setParameter("categoryName", categoryNameParameter)
				.getResultList();
		
		// then
		assertNotNull(menuList);
		menuList.forEach(System.out::println);
		
	}
}
