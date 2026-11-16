package com.bear.studyboard.post.entity;

import com.bear.studyboard.category.entity.Category;
import com.bear.studyboard.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "study_posts")
@Getter
// JPA가 DB에서 조회한 데이터로 객체를 만들 때 쓰는 기본 생성자. 외부에서 빈 객체를 만들지 못하게 protected로 막는다.
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(name = "reference_url", length = 2048)
    private String referenceUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // 새 게시글을 만든다. 작성자와 작성 시각은 이후에 바뀌지 않는다.
    public StudyPost(Member author, Category category, String title, String body, String referenceUrl,
                     LocalDateTime now) {
        this.author = author;
        this.createdAt = now;
        revise(category, title, body, referenceUrl, now);
    }

    public void revise(Category category, String title, String body, String referenceUrl, LocalDateTime now) {
        this.category = category;
        this.title = title;
        this.body = body;
        this.referenceUrl = referenceUrl;
        this.updatedAt = now;
    }
}
