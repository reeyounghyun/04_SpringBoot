package com.study.springai.section02;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "02.Prompt")
@RestController
@RequiredArgsConstructor
public class Step01UserPromptController {

    private final ChatClient chatClient;

    /*
    * 프롬프트
    *  - LLM에게 보내는 입력 전체
    * - 메시지 목록으로 구성되어 있고, 메시지에는 역할이 붙는다.
    *
    * 역할(role) 종류
    * - system: 개발자가 정하는 규칙(말투, 역할, 금지사항)
    * - user: 사용자가 입력한 질문
    * - assistant: LLM이 이전에 했던 답변과 같은 내용들
    *
    * */

    @GetMapping("/section02/step01")
    public String step01(@RequestParam String question) {

        return chatClient
                .prompt()
                .user(question)
                .call()
                .content();
    }


    /*
    * SystemPrompt
    * - 개발자가 LLM에게 주는 지침 (클라이언트에 보이지 않음)
    * - LLM은 System메시지 우선하는 지침으로 취급
    * */
    @GetMapping("/section02/step02")
    public String step02(@RequestParam String question) {

        return chatClient
                .prompt()
                .system("너는 초등학생을 가르치는 선생님이야. 쉬운 단어만 사용해서 3문장 이내로 답해.")
                .user(question)
                .call()
                .content();
    }

    @GetMapping("/section02/step02/expert")
    public String step02_expert(@RequestParam String question) {

        return chatClient
                .prompt()
                .system("너는 10년차 백엔드 개발자야. 전문용어를 사용해서 핵심만 3문장 이내로 답해.")
                .user(question)
                .call()
                .content();
    }

}
