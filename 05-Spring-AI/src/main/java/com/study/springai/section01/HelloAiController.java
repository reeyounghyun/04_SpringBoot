package com.study.springai.section01;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "01. Spring AI 시작하기")
@RestController
@RequiredArgsConstructor
public class HelloAiController {

    private final ChatClient chatClient;

    @GetMapping("/section01/hello")
    public String hello() {

        /*
        * chatClient
        * - LLM 모델과 실제 HTTP 통신을 하는 객체이다.
        *
        * 동기 호출
        * - call() LLM의 답변이 "전부" 완성될 때까지 기다렸다가 한 번에 돌려준다.
        * */
        return chatClient
                .prompt() // 1. 프롬프트 요청을 작성
                .user("안녕?") // 2. 사용자의 질문을 프롬프트에 추가
                .call() // 3. LLM에 전송하고 답변을 기다림(동기 방식)
                .content(); // 4. 답변에서 응답 텍스트만 꺼낸다.
    }

}
