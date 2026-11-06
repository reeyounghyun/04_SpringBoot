package com.bear.section07.namedquery;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class NamedQueryTests {
	
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
	* - 동적쿼리: 엔티티 매니저가 제공하는 메소드를 이용해 JPQL을 문자열로 런타임에 쿼리를 만드는 형식   (수정 전: 메니저)
	* - 정적쿼리 : 미리 쿼리를 정의하고 변경하지 않은채 쓰는 쿼리 -> 미리 정한 코드에 이름을 붙임(네임드쿼리)   (수정 전: 쿼리르)
	*
	*
	* */
	@Test
	public void 동적쿼리를_이용한_조회_테스트() {
		
		// given
		String searchName = "한우";
		int searchCategoryCode = 4;
		
		// when
		StringBuilder jpql = new StringBuilder("SELECT m FROM menu_section07 m ");
        jpql.append("WHERE ");
        jpql.append("m.menuName LIKE '%' || :menuName || '%' ");
        jpql.append("AND ");
        jpql.append("m.categoryCode = :categoryCode ");

        System.out.println("jpql = " + jpql);
		
		TypedQuery<Menu> query = entityManager.createQuery(jpql.toString(), Menu.class);

        query.setParameter("menuName", searchName);
        query.setParameter("categoryCode", searchCategoryCode);

        List<Menu> menuList = query.getResultList();
		
		// then
		assertNotNull(menuList);
		menuList.forEach(System.out::println);
		
	}
	
	@Test
	public void 네임드쿼리를_이용한_조회_테스트() {
		
		// when
		List<Menu> menuList = entityManager.createNamedQuery("menu_section07.selectMenuList", Menu.class)
											.getResultList();
		// 엔티티에 createNamedQuery의 이름을 붙여준다.


		// then
		assertNotNull(menuList);
		menuList.forEach(System.out::println);
	}
}
