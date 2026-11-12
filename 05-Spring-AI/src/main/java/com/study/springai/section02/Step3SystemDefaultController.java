package com.study.springai.section02;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "02-01.Prompt")
@RestController
public class Step3SystemDefaultController {

    private final ChatClient chatClient;

    // 메소드가 실행될 때 필드에 담긴 chatClient 객체를 꺼내 쓰고, 이 객체가 자기 안에 저장된 설정을 보고 요청을 만든다.
    // 즉 아래와 같이 defaultSystem을 설정해 두면, 사용자가 질문을 해서 메소드가 실행될 때 이 설정이 함께 전달되어 AI가 역할에 맞게 답변해 준다.
    public Step3SystemDefaultController(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("너는 스터디 게시판의 도우미이다. 항상 한국어로, 2문장 이내로 답한다.")
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    // system을 지정안했을때는 defaultSystem의 내용이 System 메시지로 전달됨.
    @GetMapping("/section02/chap03")
    public String chap03(@RequestParam String question) {

        return chatClient
                .prompt()
                .user(question)
                .call()
                .content();
    }

    // defaultSystem은 무시되고 System 메시지가 변경되어 전달됨.
    @GetMapping("/section02/chap03/overide")
    public String chap03_1(@RequestParam String question) {

        return chatClient
                .prompt()
                .system("Answer in English, in one short sentence")
                .user(question)
                .call()
                .content();
    }

}
