package com.bear.studyboard.post.service;

import com.bear.studyboard.common.error.BusinessException;
import com.bear.studyboard.common.error.ErrorCode;
import com.bear.studyboard.post.dto.PostDetailResponseDTO;
import com.bear.studyboard.post.dto.PostSummaryResponseDTO;
import org.springframework.core.io.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor
public class PostSummaryService {
// 게시글 AI 요약  Service

    private final StudyPostService studyPostService;
    private final ChatClient chatClient;

    /*
    * Resource
    * - 파일이나 URL 같은 자원을 같은 방식으로 읽을 수 있게 해주느 인터페이스*/
    private final Resource systemPrompt;
    private final Resource userPrompt;

    public PostSummaryService(
            StudyPostService studyPostService,
            ChatClient chatClient,
            @Value("classpath:/prompt/summary/system.st") Resource SYSTEM_PROMPT,
            @Value("classpath:/prompt/summary/user.st") Resource USER_PROMPT) {
        this.studyPostService = studyPostService;
        this.chatClient = chatClient;
        this.systemPrompt = SYSTEM_PROMPT;
        this.userPrompt = USER_PROMPT;
    }
    // @트렌직션을 붙이지 않는이유? -> DB 커넥션이 필요가 없기 때문
    // 외부 API 호춯은 트렌젝션 밖에서 해야 한다. (기능: AI,결제,메일방송 등)
    public PostSummaryResponseDTO summarize(long postId) {

        // 1. 게시글 찾아오기 (DTO로 찾아오기)
        PostDetailResponseDTO postDetailResponseDTO = studyPostService.findPost(postId);

        // 모델을 호출할 떄의 3가지 단계 (AI한테 요청)
        /*
         *   1. 요청 만들기(Prompt)
         *   2.프롬포트 만들어서 보내기 (처리)
         *   3.프롬포트 만든 응답을 원하는 형태를 (꺼내기)
         */

        // 요청만들기(Prompt) -> 보내기 (처리) -> 답 꺼내기
        String content = "제목 : " + postDetailResponseDTO.getTitle() + System.lineSeparator() + postDetailResponseDTO.getBody(); // 내가 보내고 싶은 컨탠츠
        String summary;

       try {
            ChatClient.ChatClientRequestSpec request = chatClient.prompt()
                    .system(systemPrompt)
                    .user(user -> user
                            .text(userPrompt)
                            .param("post", content));

            // LLM 모델에 요청을 보내고 응답을 받음
            ChatClient.CallResponseSpec response = request.call();

            summary = response.content();
        } catch (RuntimeException exception) {

           // 우리 예외로 바꾸기!
           throw new BusinessException(ErrorCode.AI_REQUEST_FAILED, "잠시후 다시 시도하세요...");
       }
        // AI 응답 검증하기
        if(summary == null || summary.isBlank()) {
            throw new BusinessException(ErrorCode.AI_RESPONSE_INVALID, "요약결과가 비어있습니다.");
        }

        return new PostSummaryResponseDTO(postDetailResponseDTO.getId(), postDetailResponseDTO.getTitle(), summary);
    }
}
