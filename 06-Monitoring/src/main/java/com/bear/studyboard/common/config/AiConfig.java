package com.bear.studyboard.common.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration(proxyBeanMethods = false)
public class AiConfig {


    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        // 1. 프롬프트와 응답을 로그로 남기는 Advisor를 만든다.
        SimpleLoggerAdvisor loggerAdvisor = new SimpleLoggerAdvisor();
        // 2. 모든 요청에 이 Advisor가 적용되도록 빌더에 등록한다.
        ChatClient.Builder configuredBuilder = builder.defaultAdvisors(loggerAdvisor);
        // 3. ChatClient를 만들어 빈으로 등록한다.
        return configuredBuilder.build();
    }
}
