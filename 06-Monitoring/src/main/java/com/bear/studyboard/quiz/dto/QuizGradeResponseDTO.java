package com.bear.studyboard.quiz.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QuizGradeResponseDTO {

    private long quizId;

    private boolean correct;

    private String feedback;

    private String answer;

    private String gradeBy; // 누가 채점했는지
}
