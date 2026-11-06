package com.bear.section01.manytoone;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ManyToOneAssociationTests {

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
	* 연관관계 : 엔티티 클래스 간의 관계를 매핑하는 것
	* 1. 다중성에 의한 분류
	* - N:1(ManyToOne)
	* - 1:N(OneToMany)
	* - 1:1(OneToOne)
	* - N:M(ManyToMany)
	* 2. 방향에 따른 분류
	* - 단방향 연관관계
	* - 양방향 연관관계
	* */
	@Test
	public void 다대일_연관관계_객체_그래프_탐색을_이용한_조회_테스트() {

		// given
		int menuCode = 15;

		// when
		/*
		* @ManyToOne의 기본 로딩 전략 EAGER
		* - find 될 때 연관된 카테고리도 로딩된다.
		* - JOIN은 보장되지 않음! (실제 SQL은 JPA 구현체, 조회방식의 차이에 따라 JOIN 또는 별도의 SELECT로 가지고 올 수도 있음)
		* */
		MenuAndCategory foundMenu = entityManager.find(MenuAndCategory.class, menuCode);
		System.out.println("조회전");
		Category menuCategory = foundMenu.getCategory();

		// then
		assertNotNull(menuCategory);
		System.out.println("foundMenu = " + foundMenu);
		System.out.println("menuCategory = " + menuCategory);
	}

	@Test
	public void 다대일_연관관계_객체지향쿼리_사용한_카테고리_이름_조회_테스트() {

		// given
		String jpql = "SELECT c.categoryName " +     // c.categoryName = 카테고리 이름
		              "FROM menu_and_category m " +  // menu_and_category = 엔티티명
		              "JOIN m.category c " +         // m.category = 연관된 Category 필드
		              "WHERE m.menuCode = 15";

		// when
		String category = entityManager.createQuery(jpql, String.class).getSingleResult();

		// then
		assertNotNull(category);
		System.out.println("category = " + category);
	}


	@Test
	public void 다대일_연관관계_객체_삽입_테스트() {

		// given
		MenuAndCategory menuAndCategory = new MenuAndCategory();
		menuAndCategory.setMenuCode(99999);
		menuAndCategory.setMenuName("죽방멸치빙수");
		menuAndCategory.setMenuPrice(30000);

		Category category = new Category();
		category.setCategoryCode(33333);
		category.setCategoryName("신규카테고리");
		category.setRefCategoryCode(null);

		menuAndCategory.setCategory(category);
		menuAndCategory.setOrderableStatus("Y");
		// DB에 없어 새 메뉴에 새 카테고리에 DB에 저장하려고 했지만, 저장이 안됨
		// 즉 cascade가 없으면 메뉴를 persist 해도 새 Category는 같이 저장되지 않는다.
		// 아직 저장 안 된 카테고리를 참조하게 되니 commit할 때 TransientPropertyValueException 같은 오류가 납니다.
		// MenuAndCategory에 @ManyToOne(cascade = CascadeType.PERSIST)를 설정하면 카테고리도 같이 저장돼요.

		// when
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin(); // 트랜잭션 시작

		entityManager.persist(menuAndCategory); // 영속화
		// "menuAndCategory를 영속 상태로 만들겠다(JPA가 관리하게 하겠다"
		entityTransaction.commit(); // 트랜잭션 종료

		/*
		* persist()에 전달한 Entity는 menuAndCategory 하나뿐인데 Category도 같이 저장된다.
		* -> MenuAndCategory의 ManyToOne에 CascadeType.PERSIST로 영속성 전파를 해놨기 때문이다.
		* 설정하지 않으면 비영속 객체 관련 예외 발생!
		* */

		// then
		MenuAndCategory foundMenuAndCategory = entityManager.find(MenuAndCategory.class, 99999);

		assertEquals(99999, foundMenuAndCategory.getMenuCode());
		assertEquals(33333, foundMenuAndCategory.getCategory().getCategoryCode());

	}

}







