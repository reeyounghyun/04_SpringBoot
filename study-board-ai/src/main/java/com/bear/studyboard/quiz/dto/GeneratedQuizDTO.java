package com.bear.studyboard.quiz.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GeneratedQuizDTO {
// 게시글로 AI 퀴즈 만들기 DTO

    private String question; // AI가 만든 문제
    private String answer; // AI가 만든 정답
}
