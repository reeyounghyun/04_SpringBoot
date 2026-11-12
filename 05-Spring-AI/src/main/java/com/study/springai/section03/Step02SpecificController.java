package com.study.springai.section03;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "03-02. 구체적으로 지시하자 ")
@RestController
@RequestMapping("/section03/step02")
@RequiredArgsConstructor
public class Step02SpecificController {

    private final ChatClient chatClient;

    private static final String POST = """
            안녕하세요. 이번 주 토요일 오후 2시에 강남역 스터디룸에서 스프링 스터디 3회차를 진행합니다.
            지난 시간에 다룬 JPA 연관관계 매핑을 복습하고, 이번에는 트랜잭션과 영속성 컨텍스트를 다룹니다.
            각자 게시판 프로젝트에 댓글 기능을 구현해 오시면 됩니다. 노트북은 꼭 지참해 주세요.
            참석이 어려우신 분은 금요일까지 댓글로 알려 주세요.
            """;

    @GetMapping("/before")
    public String before() {

        return chatClient
                .prompt()
                .user(u -> u
                        .text("이 글 요약해줘. {post}")
                        .param("post", POST)
                )
                .call()
                .content();
    }

    @GetMapping("/after")
    public String after() {

        // 분량, 형식, 말투, 포함할 내용을 구체적으로 적어줄수록 원하는 결과물을 얻기 쉽다.
        // LLM이 문장을 이해할 때, "~용"이라는 표현 쓰면 안돼(부정표현을 줄이는게 좋다) 이것보다 "~이렇게 해라" 가 더 잘 지켜진다.
        // 시스템 프롬프트를 작성할 땐 부정표현을 줄이는게 좋다.
        // 구분을 하기 위해 .(점) 이나 ,(쉼표) 의 표현을 작성하는게 좋다.
        return chatClient
                .prompt()
                .user(u -> u
                        .text("""
                                아래 게시글을 요약한다.
                                - 정확히 2문장으로 쓴다.
                                - 일시, 장소, 준비물을 반드시 포함한다.
                                - "~합니다." 체로 쓴다.
                                - 인사말과 제목은 쓰지 않는다.
                                
                                게시글:
                                {post}
                                """)
                        .param("post", POST)
                )
                .call()
                .content();
    }

}
