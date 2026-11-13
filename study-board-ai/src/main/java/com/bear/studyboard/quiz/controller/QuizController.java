package com.bear.studyboard.quiz.controller;

import com.bear.studyboard.common.dto.ApiResponse;
import com.bear.studyboard.common.error.ApiExceptionHandler;
import com.bear.studyboard.quiz.dto.QuizGradeResponseDTO;
import com.bear.studyboard.quiz.dto.QuizResponseDTO;
import com.bear.studyboard.quiz.service.QuizAIService;
import com.bear.studyboard.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            @RequestBody(required = false) Integer count // 문제 갯소
    ) {
        List<QuizResponseDTO> quizzes = quizAIService.generateQuizzes(postId, count);

        // 응답 만들기
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
        QuizGradeResponseDTO quizGradeResponseDTO = quizAIService.gradeQuiz(quizId, answer);

        // 응답 만들기
        ApiResponse<QuizGradeResponseDTO> apiResponse =
                ApiResponse.of(HttpStatus.OK, "채점 완료", quizGradeResponseDTO);

        return ResponseEntity.ok(apiResponse);
    }
}
