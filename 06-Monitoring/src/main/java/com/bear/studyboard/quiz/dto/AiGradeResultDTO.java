package com.bear.studyboard.quiz.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AiGradeResultDTO { // AI한테 응답받을 구조화 출력

    private boolean correct;
    private String feedback;
}
