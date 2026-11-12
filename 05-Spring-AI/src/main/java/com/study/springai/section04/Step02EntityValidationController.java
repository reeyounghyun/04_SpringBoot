package com.study.springai.section04;

import com.study.springai.section04.dto.PostAnalysis;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "04-02. 객체로 응답받기")
@RestController
@RequiredArgsConstructor
public class Step02EntityValidationController {

    private final ChatClient chatClient;

    private static final String POST = """
            JPA로 게시글 목록을 조회하는데 쿼리가 수십 번 나가서 원인을 찾아봤습니다.
            게시글마다 작성자를 지연 로딩으로 가져오면서 N+1 문제가 발생한 것이었고,
            fetch join으로 바꾸니 쿼리 한 번으로 줄었습니다. 같은 문제 겪는 분들께 도움이 되길 바랍니다.
            """;

    @GetMapping("/section04/step02/validate")
    public PostAnalysis hello() {

        return chatClient
                .prompt()
                .user("""
                        스프링 AI의 구조화 응답 기능을 분석해서 요약, 카테고리, 태그 3개를 만들어줘
                        """)
                .call()
                .entity(PostAnalysis.class, spec -> spec.validateSchema());

        /*
        * validateSchema()
        * - 응답 JSON이 DTO에서 만든 스키마와 일치하는지 검사
        * - 검증에 실패하면 오류 내용을 프롬프트에 추가하여 LLM에 다시 요청
        * - 기본적으로 3회 재시도, 실패하면 예외 발생
        * */
    }

    @GetMapping("/section04/step02/strict")
    public PostAnalysis hello2() {

        return chatClient
                .prompt()
                .user("""
                        스프링 AI의 구조화 응답 기능을 분석해서 요약, 카테고리, 태그 3개를 만들어줘
                        """)
                .call()
                .entity(PostAnalysis.class,
                        spec -> spec
                        .useProviderStructuredOutput()
                        .validateSchema());

        /*
        * useProviderStructuredOutput()
        * - OpenAI에 응답 규격을 제약조건으로 전달 (네이티브 구조화 출력)
        * - 모델이 처음부터 DTO에 맞는 응답을 만들도록 강제해 실패 가능성을 줄인다.
        * */
    }

}
