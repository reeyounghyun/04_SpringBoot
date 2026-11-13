package com.bear.studyboard.quiz.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AiHintResultDTO { // AI한테 응답받을 구조화 출력
// AI 퀴즈 힌트 DTO

    private String hint; // AI가 만든 힌트 (정답을 직접 말하지 않는 1~2문장)
}
