package com.bear.studyboard.quiz.service;

import com.bear.studyboard.common.error.BusinessException;
import com.bear.studyboard.common.error.ErrorCode;
import com.bear.studyboard.quiz.dto.AiGradeResultDTO;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class QuizAiClient {

    private final ChatClient chatClient;
    private final String gradingModel;
    private final Resource gradeSystemPrompt;
    private final Resource gradeUserPrompt;

    public QuizAiClient(ChatClient chatClient,
                        @Value("${study-board.ai.grading-model}") String gradingModel,
                        @Value("classpath:/prompt/grade/system.st") Resource gradeSystemPrompt,
                        @Value("classpath:/prompt/grade/user.st") Resource gradeUserPrompt) {
        this.chatClient = chatClient;
        this.gradingModel = gradingModel;
        this.gradeSystemPrompt = gradeSystemPrompt;
        System.out.println("gradeSystemPrompt = " + gradeSystemPrompt);
        this.gradeUserPrompt = gradeUserPrompt;
        System.out.println("gradeUserPrompt = " + gradeUserPrompt);
    }

    public String gradingModel() {
        return gradingModel;
    }

    public AiGradeResultDTO grade(String question, String answer, String submittedAnswer) {
        try {
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

            // 원하는 형태로 응답 가공하기
            return response.entity(AiGradeResultDTO.class);
        } catch (RuntimeException exception) {
            throw new BusinessException(ErrorCode.AI_REQUEST_FAILED);
        }
    }
}
