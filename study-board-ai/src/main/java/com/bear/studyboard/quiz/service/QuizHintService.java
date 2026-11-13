package com.bear.studyboard.quiz.service;

import com.bear.studyboard.common.error.BusinessException;
import com.bear.studyboard.common.error.ErrorCode;
import com.bear.studyboard.quiz.dto.AiHintResultDTO;
import com.bear.studyboard.quiz.dto.QuizHintResponseDTO;
import com.bear.studyboard.quiz.dto.QuizResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizHintService {

    private final QuizService quizService;
    private final QuizAiClient quizAiClient;

    public QuizHintResponseDTO getHint(Long quizId) {
        if (quizId == null || quizId <= 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "문제를 찾을 수 없습니다.");
        }

        // AI에게 힌트 요청
        QuizResponseDTO quiz = quizService.findQuiz(quizId);

        // 응답 DTO로 변환
        AiHintResultDTO result = quizAiClient.gradeHint(quiz.getQuestion(), quiz.getAnswer());

        return new QuizHintResponseDTO(quizId, quiz.getQuestion(), result.getHint());
    }






}