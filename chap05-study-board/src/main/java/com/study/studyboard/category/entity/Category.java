package com.study.studyboard.category.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "categories") // 테이블명
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * name = "name" 이름을 지정해도/ 안 해도 된다.
     * nullable = false (값이 비어있으면 안됨)
     * */
    // 제약조건 및 컬럼명 연결
    @Column(name = "name", nullable = false, length = 50)
    private String name;

}
