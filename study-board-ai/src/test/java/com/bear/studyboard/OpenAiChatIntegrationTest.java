package com.bear.studyboard;

import com.bear.studyboard.common.config.AiConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = {AiConfig.class},
        properties = "spring.ai.openai.api-key=${OPENAI_API_KEY}"
)
@EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
class OpenAiChatIntegrationTest {

    private final ChatClient chatClient;

    @Autowired
    OpenAiChatIntegrationTest(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Test
    void callsLlmAndReceivesResponse() {
        String response = chatClient.prompt()
                .user("1 더하기 1의 답을 숫자 하나로만 답해줘.")
                .call()
                .content();

        System.out.println("response = " + response);

        assertThat(response).isNotBlank();
        assertThat(response.strip()).isEqualTo("2");
    }


}
