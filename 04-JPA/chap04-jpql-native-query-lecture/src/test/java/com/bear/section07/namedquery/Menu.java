package com.bear.section07.namedquery;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


import jakarta.persistence.*;

@Entity(name="menu_section07")
@Table(name="tbl_menu")
@NamedQueries({
// NamedQuery 앞단에 쿼리를 정의해놓는게 특징이다.
// 데이터베이스의 데이터들을 활용하기위해 집계함수를 쓰거나 미리 연산이 되어야할 때, 미리 만들어 놓고 사용할 수 있다.
	@NamedQuery(name="menu_section07.selectMenuList", query="SELECT m FROM menu_section07 m")
		// name은 앞단에, 쿼리는 뒤단에 미리 만들어 사용할 수 있다.
		// 미리 여러개 사용해 사용할 수 있다.
		// 이름으로 호출하여 사용할 수 있다.
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Menu {

	@Id
	@Column(name="menu_code")
	private int menuCode;

	@Column(name="menu_name")
	private String menuName;

	@Column(name="menu_price")
	private int menuPrice;

	@Column(name="category_code")
	private int categoryCode;

	@Column(name="orderable_status")
	private String orderableStatus;
}
