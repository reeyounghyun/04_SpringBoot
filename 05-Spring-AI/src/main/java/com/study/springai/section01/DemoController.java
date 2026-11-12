package com.study.springai.section01;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "00. ")
@RestController
@RequiredArgsConstructor
public class DemoController {

    private final ChatClient chatClient;

    @GetMapping("/section01/demo")
    public String hello() {

        return chatClient
                .prompt() // 프롬프트 요청을 작성
                .user("안녕.") // 사용자의 질문을 프롬프트에 추가
                .call() // LLM에 전송하고 답변이 다 올 때까지 기다림(동기 방식)
                .content(); // 응답에서 답변 텍스트만 꺼냄
    }

}
