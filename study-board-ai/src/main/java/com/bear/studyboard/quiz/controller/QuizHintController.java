package com.bear.studyboard.quiz.controller;

import com.bear.studyboard.common.dto.ApiResponse;
import com.bear.studyboard.quiz.dto.QuizHintResponseDTO;
import com.bear.studyboard.quiz.service.QuizHintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QuizHintController {
    // AI가 만든 힌트
    private final QuizHintService quizHintService;

    @GetMapping("/quizzes/{quizId}/hint")
    public ResponseEntity<ApiResponse<QuizHintResponseDTO>> findHint(
            @PathVariable long quizId
    ) {
        QuizHintResponseDTO quizHintResponseDTO = quizHintService.getHint(quizId);

        // 응답 만들기
        ApiResponse<QuizHintResponseDTO> apiResponse =
                ApiResponse.of(HttpStatus.OK, "힌트 완성", quizHintResponseDTO);

        return ResponseEntity.ok(apiResponse);
    }
}
