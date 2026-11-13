package com.bear.studyboard.quiz.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AiGradeResultDTO {// AI한테 응답받을 구조화 출력
// AI 퀴즈 채점 DTO

    private boolean correct;  // AI가 판단한 정답 여부
    private String feedback;  // AI가 알려주는 채점 설명
}
