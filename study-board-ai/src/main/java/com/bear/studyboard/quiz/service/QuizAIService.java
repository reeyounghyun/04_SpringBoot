package com.bear.studyboard.quiz.service;

import com.bear.studyboard.common.error.BusinessException;
import com.bear.studyboard.common.error.ErrorCode;
import com.bear.studyboard.post.dto.PostDetailResponseDTO;
import com.bear.studyboard.post.service.StudyPostService;
import com.bear.studyboard.quiz.dto.*;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
//@RequiredArgsConstructor
public class QuizAIService {
    // AI 퀴즈 채점 Service

    private static final int DEFAULT_QUIZ_COUNT = 3; // 문제의 갯수를 정하지 않았을때 기본값 3으로 설정함.
    private static final int MAX_QUIZ_COUNT = 5; // 최대 문제수를 5문제로 설정, 토큰이 많이 나가는걸 방지하기 위한 설정.

    private final Resource generateQuizSystemPrompt; // 시스템
    private final Resource generateQuizUserPrompt; // 사용자
    private final StudyPostService studyPostService;
    private final ChatClient chatClient;
    private final QuizService quizService;
    private final QuizAiClient quizAiClient;

    public QuizAIService(
            @Value("classpath:/prompt/generateQuiz/system.st") Resource generateQuizSystemPrompt,
            @Value("classpath:/prompt/generateQuiz/user.st") Resource generateQuizUserPrompt,
            StudyPostService studyPostService,
            ChatClient chatClient,
            QuizService quizService,
            QuizAiClient quizAiClient) {
        this.generateQuizSystemPrompt = generateQuizSystemPrompt;
        this.generateQuizUserPrompt = generateQuizUserPrompt;
        this.studyPostService = studyPostService;
        this.chatClient = chatClient;
        this.quizService = quizService;
        this.quizAiClient = quizAiClient;
    }

    public List<QuizResponseDTO> generateQuizzes(long postId, Integer requestCount) {
        // postId, count 값을 검증 ->  count가 null이거나 너무 크면 기본값을 쓰거나 예외를 던지는 단계
        int count = resolveCount(requestCount);

        // 문제를 만들기 -> AI(ChatClient)를 호출해서 퀴즈를 만드는 단계
        /*1.프롬프트 재료준비*/
        PostDetailResponseDTO postDetailResponseDTO = studyPostService.findPost(postId);
        String title = postDetailResponseDTO.getTitle();
        String body = postDetailResponseDTO.getBody();


        // 프롬프트 요청
        GeneratedQuizSetDTO generatedQuizSet = generatedQuizSet(title, body, count);

        // AI가 응답한 결과를 검증
        List<GeneratedQuizDTO> validQuizzes = validateGeneratedQuizzes(generatedQuizSet, count);


        // 문제 만든걸 저장 함 ->  Quiz 엔티티로 DB에 저장하는 단계
        List<QuizResponseDTO> quizResponseDTOList = quizService.saveQuizzes(postDetailResponseDTO.getId(), validQuizzes);

        // 사용자 응답  QuizResponseDTO -> 저장한 Quiz를 QuizResponseDTO.from()으로 바꿔서 반환하는 단계

        return quizResponseDTOList;
    }

    public GeneratedQuizSetDTO generatedQuizSet(String title, String body, int requestCount) {
        try {
            //프롬프트 요청 준비하기
            ChatClient.ChatClientRequestSpec request = chatClient.prompt()
                    .system(generateQuizSystemPrompt)
                    .user(u -> u
                            .text(generateQuizUserPrompt)
                            .param("count", String.valueOf(requestCount))
                            .param("title", title)
                            .param("body", body)
                    );
            // 응답 받기
            ChatClient.CallResponseSpec response = request.call();

            // 원하는 형태로 응답 가공
            return response.entity(GeneratedQuizSetDTO.class);
        } catch (RuntimeException exception) {
            // 우리 예외로 바꾸기!
            throw new BusinessException(ErrorCode.AI_REQUEST_FAILED);
        }
    }

    private static List<GeneratedQuizDTO> validateGeneratedQuizzes(GeneratedQuizSetDTO generated, int count) {

        // 목록이 없는 경우
        if (generated == null || generated.getQuizzes() == null) {
            throw new BusinessException(ErrorCode.AI_RESPONSE_INVALID, "생성된 문제가 없습니다.");
        }

        // 목록을 검증한 후 있는 검증된 내용들만 담아줄 리스트 준비
        List<GeneratedQuizDTO> generatedQuizzes = generated.getQuizzes();
        List<GeneratedQuizDTO> validQuizzes = new ArrayList<>(); // 검증을 위해 하나 더

        for (GeneratedQuizDTO quiz : generatedQuizzes) {
            if (quiz == null) {
                continue; // 다음 반복으로
            }
            String question = quiz.getQuestion();
            String answer = quiz.getAnswer();

            // [조건] question가 널이거나, question가
            if (question == null || question.isBlank() || question.length() > 500) {
                continue;
            }

            if (answer == null || answer.isBlank() || answer.length() > 200) {
                continue;
            }
            validQuizzes.add(quiz);

            if (validQuizzes.size() == count) {
                break;
            }
        }

        if (validQuizzes.isEmpty()) {
            throw new BusinessException(ErrorCode.AI_RESPONSE_INVALID, "저장할 수 있는 문제가 없습니다.");
        }
        return validQuizzes;
    }

    // 검증로직
    private static int resolveCount(Integer count) {
        // "안 보냈으면"은 사용자가 요청할 때 문제 개수(body)를 아예 안 넣었으면
        if (count == null) {
            return DEFAULT_QUIZ_COUNT;
        }

        // 최소값은 1 최대값은 5개 이하 [조건을 추가해줌]
        if (count < 1 || count > MAX_QUIZ_COUNT) {
            throw new BusinessException(ErrorCode.INVALID_INPUT,
                    "문제수는 1개이상 " + MAX_QUIZ_COUNT + "개 이하여야합니다.");
        }
        return count;
    }

    public QuizGradeResponseDTO gradeQuiz(long quizId, String answer) {
        // 1. 요청값 확인
        // 딥변이 비어있을때 -> 2이하, 1이하 조건을 걸으면 의미없는 답을 거를수 있다.
        if(answer == null || answer.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "답을 입력해주세요");
        }
        if(answer.length() > 500 || answer.length() < 2) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "답을 제대로 입력해주세요");
        }

        // 2. 문제 정보를 가져오기
        QuizResponseDTO quizResponseDTO = quizService.findQuiz(quizId);

        // 3. 채점

        // 3-1. AI를 부르지 않아도 되는경우 정답이 완전히 일치한 경우
        String correctAnswer = quizResponseDTO.getAnswer();
        if(answer.equals(correctAnswer)) {
            return new QuizGradeResponseDTO(quizId, true, "정답과 일치합니다", answer, "EXACT_MATCH");
        }

        // 3-2. AI를 불러야하는 경우
        String question = quizResponseDTO.getQuestion();
        AiGradeResultDTO aiGradeResultDTO = quizAiClient.grade(question, correctAnswer, answer);

        String gradingModel = quizAiClient.gradingModel();

        // 검증
        if (aiGradeResultDTO == null || aiGradeResultDTO.getFeedback() == null || aiGradeResultDTO.getFeedback().isBlank()) {
            throw new BusinessException(ErrorCode.AI_RESPONSE_INVALID, "채점 결과가 비어있습니다.");
        }

        return new QuizGradeResponseDTO(quizId, aiGradeResultDTO.isCorrect(), aiGradeResultDTO.getFeedback(), correctAnswer, gradingModel);
    }
}
