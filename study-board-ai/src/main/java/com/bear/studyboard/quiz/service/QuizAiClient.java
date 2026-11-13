package com.bear.studyboard.quiz.service;

import com.bear.studyboard.common.error.BusinessException;
import com.bear.studyboard.common.error.ErrorCode;
import com.bear.studyboard.quiz.dto.AiGradeResultDTO;
import com.bear.studyboard.quiz.dto.AiHintResultDTO;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class QuizAiClient {
// AI 퀴즈 채점 Client

    private final ChatClient chatClient;
    private final String gradingModel;
    private final Resource gradeSystemPrompt;
    private final Resource gradeUserPrompt;

    private final Resource gradequestionPrompt;
    private final Resource gradeanswerPrompt;

    public QuizAiClient(
            ChatClient chatClient,
            @Value("${study-board.ai.grading-model}") String gradingModel,
            @Value("classpath:/prompt/grade/system.st") Resource gradeSystemPrompt,
            @Value("classpath:/prompt/grade/user.st") Resource gradeUserPrompt,
            @Value("classpath:/prompt/hint/system.st") Resource gradequestionPrompt,
            @Value("classpath:/prompt/hint/user.st") Resource gradeanswerPrompt
            ) {
        this.chatClient = chatClient;
        this.gradingModel = gradingModel;
        this.gradeSystemPrompt = gradeSystemPrompt;
        this.gradeUserPrompt = gradeUserPrompt;
        this.gradequestionPrompt = gradequestionPrompt;
        this.gradeanswerPrompt = gradeanswerPrompt;
    }

    public String gradingModel() {
        return gradingModel;
    }

    public AiGradeResultDTO grade(String question, String answer, String submittedAnswer) {
        try {
            //프롬프트 요청 준비하기
            ChatClient.ChatClientRequestSpec request = chatClient.prompt()
                    // 이 요청에 대해서만 사용할 모델을 설정
                    .options(ChatOptions.builder().model(gradingModel)) // 모델 gpt-5-nano
                    .system(gradeSystemPrompt)
                    .user(u -> u
                            .text(gradeUserPrompt)
                            .param("question", question)
                            .param("answer", answer)
                            .param("submitted", submittedAnswer)
                    );
            // 응답 받기
            ChatClient.CallResponseSpec response = request.call();

            // 원하는 형태로 응답 가공
            return response.entity(AiGradeResultDTO.class);
        } catch (RuntimeException exception) {
            // 우리 예외로 바꾸기!
            throw new BusinessException(ErrorCode.AI_REQUEST_FAILED);
        }

    }

    //힌트
    public AiHintResultDTO gradeHint(String question, String answer) {
        try {
            //프롬프트 요청 준비하기
            ChatClient.ChatClientRequestSpec request = chatClient.prompt()
                    // 이 요청에 대해서만 사용할 모델을 설정
                    .options(ChatOptions.builder().model(gradingModel)) // 모델 gpt-5-nano
                    .system(gradequestionPrompt)
                    .user(u -> u
                            .text(gradeanswerPrompt)
                            .param("question", question)
                            .param("answer", answer)

                    );
            // 응답 받기
            ChatClient.CallResponseSpec response = request.call();

            // 원하는 형태로 응답 가공
            return response.entity(AiHintResultDTO.class);
        } catch (RuntimeException exception) {
            // 우리 예외로 바꾸기!
            throw new BusinessException(ErrorCode.AI_REQUEST_FAILED);
        }

    }
}
