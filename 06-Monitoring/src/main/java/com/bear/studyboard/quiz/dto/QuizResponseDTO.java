package com.bear.studyboard.quiz.dto;


import com.bear.studyboard.quiz.entity.Quiz;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QuizResponseDTO {

    private long id;
    private long postId;
    private String question;
    private String answer;
    private LocalDateTime createdAt;

    public static QuizResponseDTO from(Quiz quiz, long postId) {

        return new QuizResponseDTO(
                quiz.getId(),
                postId,
                quiz.getQuestion(),
                quiz.getAnswer(),
                quiz.getCreatedAt());
    }
}
