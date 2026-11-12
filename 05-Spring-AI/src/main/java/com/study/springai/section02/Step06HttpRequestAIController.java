package com.study.springai.section02;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

// 스프링 AI가 이 클래스 내용을 만들어놓았기 때문에 사용하지 않는다?
public class Step06HttpRequestAIController {
    private static final URI OPENAI_CHAT_COMPLETIONS_URI =
            URI.create("https://api.openai.com/v1/chat/completions");

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String model;
    private final Double temperature;

    public Step06HttpRequestAIController(
            ObjectMapper objectMapper,
            @Value("${spring.ai.openai.api-key}") String apiKey,
            @Value("${spring.ai.openai.chat.model}") String model,
            @Value("${spring.ai.openai.chat.temperature}") Double temperature) {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
        this.model = model;
        this.temperature = temperature;
    }

    @GetMapping("/section02/step06")
    public String directHttp(@RequestParam(defaultValue = "DI가 뭐야?") String question)
            throws IOException, InterruptedException {

        // Map.of 담을 내용
        Map<String, Object> requestBody = Map.of(
                "model", model,
                "temperature", temperature,
                "messages", List.of(
                        Map.of(
                                "role", "system",
                                "content", "너는 신입 개발자를 가르치는 강사이다. 2문장 이내로 답한다."),
                        Map.of(
                                "role", "user",
                                "content", question)));

        String requestJson = objectMapper.writeValueAsString(requestBody);

        // JAVA에서 HTTP 요청을 보내는
        HttpRequest request = HttpRequest.newBuilder()
                .uri(OPENAI_CHAT_COMPLETIONS_URI)
                .timeout(Duration.ofSeconds(60))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestJson)) // HttpRequest.BodyPublishers.ofString(requestJson)) 요 내용은 프롬프트가 들어갈 자리임.
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IllegalStateException(
                    "OpenAI API 요청 실패: status=" + response.statusCode() + ", body=" + response.body());
        }

        JsonNode responseJson = objectMapper.readTree(response.body());
        String answer = responseJson.path("choices")
                .path(0)
                .path("message")
                .path("content")
                .asString();
        JsonNode usage = responseJson.path("usage");

        return answer;
    }
}
