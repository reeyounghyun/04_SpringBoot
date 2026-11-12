package com.study.springai.section03;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
 * 답변에 필요한 자료를 프롬프트에 함께 넣어 주자
 */
@Tag(name = "03-05. 자료를 주기 ")
@RestController
@RequestMapping("/section03/step05")
@RequiredArgsConstructor
public class Step05GroundingController {

    private final ChatClient chatClient;

    // db에 있는 공지
    private static final String NOTICE = """
            [스프링 스터디 운영 안내]
            - 일시: 매주 토요일 오후 2시 ~ 5시
            - 장소: 강남역 스터디룸 3호실
            - 준비물: 노트북
            - 결석 시 금요일까지 게시판 댓글로 알린다.
            """;


    @GetMapping("/before")
    public String before(@RequestParam String question) {

        return chatClient
                .prompt()
                .user(u -> u
                        .text(question)
                )
                .call()
                .content();
    }

    @GetMapping("/after")
    public String after(@RequestParam String question) {

        return chatClient
                .prompt()
                .system(s -> s
                        .text("""
                                <notice> 태그 안의 공지사항만 근거로 질문에 답한다.
                                공지사항에 없는 내용은 추측하지 말고 "공지에 없는 내용입니다"라고 답한다.
                                
                                <notice>
                                {notice}
                                </notice>
                                """)
                        .param("notice", NOTICE))
                .user(u -> u
                        .text(question)
                )
                .call()
                .content();
    }
}