package com.study.springai.section02;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "02-02. Prompt Template")
@RestController
@RequiredArgsConstructor
public class Step04PromptTemplateController {

    private final ChatClient chatClient;

    @GetMapping("/section02/step04")
    public String hello(
            @RequestParam String topic, // 주제
            @RequestParam String level  // 수준
    ) {

        /*
        * 프롬프트 템플릿
        * - 프롬프트에 {변수} 자리를 만들고 호출할 때 값을 채워넣는 방식
        * - 프롬프트(고정), 데이터(변동)이 코드에서 분리되어 가독성이 좋음.
        * - 같은 문장을 여러 값으로 재사용 가능
        * */
        return chatClient
                .prompt()
                .system(s -> s
                        .text("너는 {level}에게 개발 용어를 설명하는 강사야. 3문장 이내로 설명해.")
                        .param("level", level)
                )
                .user(u -> u
                        .text("{topic}이(가) 무엇인지 설명해줘.")
                        .param("topic", topic)
                )
                .call()
                .content();
    }

}
