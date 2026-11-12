package com.study.springai.section02;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "02-03. 요청구조 ")
@RestController
public class Step05RequestStructureController {

    private final ChatModel chatModel;

    //  ChatModel Bean을 받아서 chatModel 필드에 넣어 주는 통로
    public Step05RequestStructureController(ChatModel chatModel) { // ① Spring이 ChatModel Bean을 넣어 줌
        this.chatModel = chatModel; // ② 받은 값을 필드에 저장
    }

    @GetMapping("/section02/step05")
    public String hello(@RequestParam String question) {
        // 1. 역할별 메시지 객체를 만든다. (.system() .user())
        SystemMessage systemMessage = new SystemMessage("너는 신입 개발자를 가르치는 강사다. 2문장 이내로 답해라.");
        UserMessage userMessage = new UserMessage(question);

        // 2. 메시지 목록을 하나의 Prompt로 묶음
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        // ChatModel에 들어 있는 메소드로, 만들어 둔 Prompt를 실제로 OpenAI에 보내고 응답(ChatResponse)을 받아 오는 일을 합니다. AI 서버   와 통신이 일어나는 바로 그 지점입니다.
        // 3. ChatModel이 Prompt를 HTTP 요청으로 바꿔 OpenAI에게 전송
        ChatResponse response = chatModel.call(prompt);

        System.out.println("response.getResult() = " + response.getResult());


        System.out.println("response.getResult().getOutput() = " + response.getResult().getOutput());

        /*
         * 4. ChatResponse에서 답변 텍스트를 꺼내기
         * getResult() : 응답 전체(결과 + 모델명, 토큰 사용량 등 metadata)중 AI가 만든 결과만 꺼냄
         * getOutput() :
         * 1. AI가 한 답변 메시지(AssistantMessage)를 꺼낸다.
         * 2.결과 안의 assistant(대화에서 AI 쪽이 하는 말에 붙는 역할(role) 이름)
         *   역할 메시지를 꺼냄. SystemMessage,UserMessage와 짝이 되는 AI 쪽 메시지
         * getText() :
         * 1. 메시지에서 답변 글자만 꺼낸다.
         * 2. 객체 안에 저장되어 있는 값을 가져온다
         */
        String answer = response.getResult().getOutput().getText();

        System.out.println("answer = " + answer);

        /*
        * 입력토큰수
        * 출력토큰수
        * 두가지의 합계
        *
        * response.getMetadata()     ChatResponse의 metadata (모델명, 토큰 사용량 등)
                    .getUsage()        토큰 사용량 정보
                    .getPromptTokens()      입력 토큰: 내가 보낸 system + user 메시지 크기
                    .getCompletionTokens()  출력 토큰: AI가 만든 답변 크기
                    .getTotalTokens()       합계 = 입력 + 출력
        * */
        System.out.println("answer = " + response.getMetadata().getUsage().getPromptTokens()); // 입력토큰
        System.out.println("answer = " + response.getMetadata().getUsage().getCompletionTokens()); // 출력토큰
        System.out.println("answer = " + response.getMetadata().getUsage().getTotalTokens()); // 합계


        return "";
    }

}

