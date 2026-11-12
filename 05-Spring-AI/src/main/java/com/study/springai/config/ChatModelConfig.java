/*
package com.study.springai.config;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ChatModelConfig {

    @Bean
    @Primary
    public ChatModel chatModel(
            @Value("${spring.ai.openai.api-key}") String apiKey,
            @Value("${spring.ai.openai.chat.model}") String model,
            @Value("${spring.ai.openai.chat.temperature}") Double temperature
    ) {

        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .apiKey(apiKey)
                .model(model)
                .temperature(temperature)
                .build();

        return OpenAiChatModel.builder()
                .options(options)
                .build();
    }
}
*/
