package com.bear.studyboard.quiz.entity;

import com.bear.studyboard.post.entity.StudyPost;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "quizzes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "study_post_id", nullable = false)
    private StudyPost studyPost;

    @Column(nullable = false, length = 500)
    private String question;

    @Column(nullable = false, length = 200)
    private String answer;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // id를 제외한 나머지필드만 받는 생성자
    public Quiz(StudyPost studyPost, String question, String answer, LocalDateTime createdAt) {
        this.studyPost = studyPost;
        this.question = question;
        this.answer = answer;
        this.createdAt = createdAt;
    }
}
