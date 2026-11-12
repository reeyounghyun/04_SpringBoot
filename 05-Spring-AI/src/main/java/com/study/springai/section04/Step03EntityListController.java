package com.study.springai.section04;

import com.study.springai.section04.dto.TitleCandidate;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "04~03 목록으로 응답받기")
@RestController
@RequiredArgsConstructor
public class Step03EntityListController {

    private final ChatClient chatClient;

    @GetMapping("/section04/step03")
    public List<TitleCandidate> hello(@RequestParam String content) {

        return chatClient
                .prompt()
                .system("""
                        <post>태그 안에 게시글에 어울리는 제목 후보를 정확히 3개 추천해. 제목은 20자 내외이다.
                        """)
                .user(u -> u
                        .text("<post>{content}</post>")
                        .param("content", content))

                .call()
                .entity(new ParameterizedTypeReference<List<TitleCandidate>>() {
                });
    }

}
