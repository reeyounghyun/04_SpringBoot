package com.bear.section01.manytoone;

import jakarta.persistence.*;
import lombok.*;


@Entity(name = "menu_and_category")
@Table(name = "tbl_menu")
// tbl_menu 테이블과 매핑되는 테이블 이름
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

// class 명과 테이블 이름이 달라도 ok
public class MenuAndCategory {

    @Id
    @Column(name = "menu_code")
    private int menuCode;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "menu_price")
    private int menuPrice;

    /*
    * "옵션없는 기본값"
    * @JoinColumn(name = "category_code")
    * @ManyToOne
    * */

    @JoinColumn(name = "category_code",
            nullable = true,    // NULL 허용여부
            unique = false,     // 외래키의 UNIQUE 제약조건
            insertable = true,  // 외래키 컬럼을 SQL INSERT에 포함할지
            updatable = true    // 외래키 컬럼을 SQL UPDATE에 포함할지
    )

    /*
    * 매핑 어노테이션 설정
    * - fetch: 연관 엔티티의 조회 전략 지정 (EAGER, LAZY)
    * - optional: 연관 엔티티가 없어도 되는지 지정 (default: true)
    * - cascade: 현재 엔티티의 영속성 작업을 연관 엔티티에도 전파할지 설정함 (PERSIST, MERGE, REMOVE, REFRESH, DETACH)
    * - PERSIST: 현제 엔티티를 저장할때 연관 엔티티도 같이 저장
    * - MERGE: 현제 엔티티를 병합할때 연관 엔티티도 병합
    * - REMOVE: 현제 엔티티를 삭제할때 연관 엔티티도 삭제
    * - REFRESH: 현제 엔티티를 새로고침(다시조회)할때 연관 엔티티도 refresh
    * - ALL : 모두 적용
    * - DETACH: 현제 엔티티를 detach할때 연관 엔티티도 detach
    * */
    // @ManyToOne  // fetch : EAGER, cascade : 없음, optional : true
    @ManyToOne(cascade = CascadeType.PERSIST , fetch = FetchType.LAZY)
    private Category category;

    @Column(name = "orderable_status")
    private String orderableStatus;

}
