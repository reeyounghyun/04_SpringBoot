package com.bear.studyboard.quiz.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QuizGradeResponseDTO {
    // AI 퀴즈 채점 DTO

    private long quizId;      // 채점한 퀴즈 번호
    private boolean correct;  // 정답 여부
    private String feedback;  // AI가 알려주는 채점 설명
    private String answer;
    private String gradeBy; // 누가 채점했는지
}
