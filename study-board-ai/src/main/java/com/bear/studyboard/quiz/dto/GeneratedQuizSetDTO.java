package com.bear.studyboard.quiz.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GeneratedQuizSetDTO {
    // 게시글로 AI 퀴즈 만들기 DTO

    private List<GeneratedQuizDTO> quizzes; // 퀴즈 1개짜리(GeneratedQuizDTO)를 여러 개 담은 목록
}
