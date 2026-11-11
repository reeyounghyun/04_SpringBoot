package com.study.studyboard.comment.entity;

import com.study.studyboard.member.entity.Member;
import com.study.studyboard.post.entity.StudyPost;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*FetchType.LAZY : 연관된 엔티티를 실제로 사용하는 순간에 조회(지연 로딩)*/
    /*optional = false : 이 연관관계가 반드시 있어야 한다 즉 null이면 안된다.
    *optional = true(기본값) : 이 연관관계가 없어도 된다 즉 null 허용.*/
    @ManyToOne(fetch = FetchType.LAZY, optional = false)

    /*
    * @JoinColumn : 연관관계를 맺을 때 어떤 FK 컬럼으로 조인할지
    * -comments 테이블의 name="study_post_id" 컬럼을 이용해서 StudyPost를 찾아와 */
    /*
    * nullable = false : 이 컬럼에 Null을 넣을 수 없다, 즉 DB 컬럼이 NOT NULL 임.
    * 기본값: true(null 허용)
    * 사용: 테이블을 만들 때 DDL 쓰이는 설정
    */
    @JoinColumn(name = "study_post_id", nullable = false)
    private StudyPost studyPost;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    /*@Column : 엔티티 필드를 DB 컬럼에 매핑할 때, 컬럼의 세부 정보를 저장*/
    @Column(nullable = false, length = 1000)
    private String body;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

/*DB: FK가 가리키는 쪽이 부모, FK를 가진 쪽이 자식*/
/*JPA: FK를 가진 쪽이 연관관계의 주인*/
/*PK Primary key, 기본 키 : 내 행을 구분하는 번호*/
/*FK Foreign key 외래키 : 다른 테이블의 PK를 가리키는 번호*/