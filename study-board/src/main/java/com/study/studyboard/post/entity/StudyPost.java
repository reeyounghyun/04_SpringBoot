package com.study.studyboard.post.entity;

import com.study.studyboard.category.entity.Category;
import com.study.studyboard.category.repository.CategoryRepository;
import com.study.studyboard.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity // 영속성 객체이기 때문에 Setter를 붙이지 않음.
@Getter
@Table(name = "study_posts")
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

    //자료등록: 새 게시글 객체를 만들 방법이 지금은 없어서 필요
    public static StudyPost create(Member author, Category category, String title, String body, String referenceUrl) {

        StudyPost post = new StudyPost();
        post.author = author;
        post.category = category;
        post.title = title;
        post.body = body;
        post.referenceUrl = referenceUrl;
        post.createdAt = LocalDateTime.now();
        post.createdAt = LocalDateTime.now();
        return post;
    }

    // 자료수정
    public void update(String title, String body, Category category, String referenceUrl) {
        if (title != null) this.title = title;
        if (body != null) this.body = body;
        if (category != null) this.category = category;
        if (referenceUrl != null) this.referenceUrl = referenceUrl;
        this.updatedAt = LocalDateTime.now();
    }
}

