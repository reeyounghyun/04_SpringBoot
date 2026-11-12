package com.study.springai.section03;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "03-01. 역할지정 ")
@RestController
@RequiredArgsConstructor
public class Step01RoleController {

    private final ChatClient chatClient;

    /*
    * 1. 프롬포트 앤지니어링 기초
    * Q.프롬포트 앤지니어링은?
    *  - LLM에게서 원하는 답변이 나오도록 프롬포트를 설계하는 일이다.
    * 방법
    * 1. 역할(Role)을 정해주기 : 너는 땡땡이다. LLM이 어떤 입장에서 답할지 정하기
    * 2. 맥락(Context)
    *  - 누가 읽는지, 어디에 쓰이는지
    *  - 같은 질문이라도 읽는 사람이 달라질 것이기 때문에
    * */

    @GetMapping("/section03/step01/before")
    public String before(@RequestParam String question) {

        return chatClient
                .prompt()
                .user(question)
                .call()
                .content();
    }

    @GetMapping("/section03/step01/after")
    public String after(@RequestParam String question) {

        return chatClient
                .prompt()
                .system("""
                        너는 백엔드 부트캠프의 멘토야.
                        질문자는 JPA를 이제 막 배웠고, 게시판 프로젝트를 만들고 있어.
                        게시판과 댓글로 예를 들어서 설명해.
                        """)
                .user(question)
                .call()
                .content();
    }

}
