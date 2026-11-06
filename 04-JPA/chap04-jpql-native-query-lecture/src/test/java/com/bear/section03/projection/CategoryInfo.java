package com.bear.section03.projection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/* 이 클래스는 엔티티로 설정하지 않는다.
 * 다만 여러 컬럼 정보를 의미 있는 단위로 묶기 위한 용도의 클래스이다. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryInfo {

	// 필드로 카테고리 필드와 카테고리 네임을 가져왔다
	// 묶어서 사용하기 위한 용도이다.
	private int categoryCode;
	private String categoryName;
}
