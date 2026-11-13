package com.bear.studyboard.comment.entity;

import com.bear.studyboard.member.entity.Member;
import com.bear.studyboard.post.entity.StudyPost;
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "study_post_id", nullable = false)
    private StudyPost studyPost;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    @Column(nullable = false, length = 1000)
    private String body;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // 새 댓글을 만든다. 작성 시각과 수정 시각을 같은 값으로 시작한다.
    public Comment(StudyPost studyPost, Member author, String body, LocalDateTime now) {
        this.studyPost = studyPost;
        this.author = author;
        this.createdAt = now;
        revise(body, now);
    }

    // 댓글 본문을 바꾸고 수정 시각을 갱신한다. 트랜잭션이 끝나면 변경 감지로 UPDATE된다.
    public void revise(String body, LocalDateTime now) {
        this.body = body;
        this.updatedAt = now;
    }
}
