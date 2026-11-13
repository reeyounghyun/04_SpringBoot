package com.bear.studyboard.quiz.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QuizHintResponseDTO {
    // AI 퀴즈 힌트 DTO (사용자에게 보내는 응답)

    private long quizId;      // 힌트를 요청한 퀴즈 번호
    private String question;  // 어떤 문제의 힌트인지 보여주기 위한 문제 내용
    private String hint;      // AI가 만든 힌트 (AiHintResultDTO.hint 값)
}
