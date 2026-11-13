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
// 게시글로 AI 퀴즈 만들기

    private long id;
    private long postId;
    private String Question; // "이 퀴즈가 어떤 게시글로 만들어졌는지"를 알려주는 게시글 번호
    private String answer; // 퀴즈의 정답 / GeneratedQuizDTO.answer → Quiz에 저장된 값
    private LocalDateTime createdAt; // 퀴즈가 만들어진 시각  QuizService의 LocalDateTime now가 저장된 값

    public static QuizResponseDTO from(Quiz quiz, long postId) {

        return new QuizResponseDTO(
                quiz.getId(),
                postId,
                quiz.getQuestion(),
                quiz.getAnswer(),
                quiz.getCreatedAt());
    }
}
