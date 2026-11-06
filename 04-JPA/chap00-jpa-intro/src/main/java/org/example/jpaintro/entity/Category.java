package org.example.jpaintro.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity(name="menu_category")
@Table(name="tbl_category")
// tbl_category테이블로 매핑
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Category {
	
	@Id
	@Column(name="category_code")
	private int categoryCode;

	@Column(name="category_name")
	private String categoryName;

	@Column(name="ref_category_code")
	private Integer refCategoryCode;

}
