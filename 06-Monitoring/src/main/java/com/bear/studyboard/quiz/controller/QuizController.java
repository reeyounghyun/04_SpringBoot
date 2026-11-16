package com.bear.studyboard.quiz.controller;


import com.bear.studyboard.common.dto.ApiResponse;
import com.bear.studyboard.quiz.dto.QuizGradeResponseDTO;
import com.bear.studyboard.quiz.dto.QuizResponseDTO;
import com.bear.studyboard.quiz.service.QuizAIService;
import com.bear.studyboard.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QuizController {

    private final QuizAIService quizAIService;
    private final QuizService quizService;

    @PostMapping("/posts/{postId}/quiezzes")
    public ResponseEntity<ApiResponse<List<QuizResponseDTO>>> generateQuizzes(
            @PathVariable long postId,
            @RequestBody(required = false) Integer count // 문제 개수
    ) {

        List<QuizResponseDTO> quizzes = quizAIService.generateQuizzes(postId, count);

        // 응답만들기
        HttpStatus status = HttpStatus.CREATED;
        ApiResponse<List<QuizResponseDTO>> response = ApiResponse.of(status, "문제생성 완료", quizzes);

        return ResponseEntity.status(status).body(response);
    }

    // 채점
    @PostMapping("/quizzes/{quizId}/grade")
    public ResponseEntity<ApiResponse<QuizGradeResponseDTO>> gradeQuiz(
            @PathVariable long quizId,
            @RequestBody String answer
    ) {
        QuizGradeResponseDTO grade =  quizAIService.gradeQuiz(quizId,answer);

        ApiResponse<QuizGradeResponseDTO> response = ApiResponse.of(HttpStatus.OK, "채점 완료", grade);

        return ResponseEntity.ok(response);
    }
}
