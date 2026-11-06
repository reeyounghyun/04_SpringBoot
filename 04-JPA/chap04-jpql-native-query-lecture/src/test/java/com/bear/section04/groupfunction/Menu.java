package com.bear.section04.groupfunction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name="menu_section04")
@Table(name="tbl_menu")
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
