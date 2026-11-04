package org.example.restapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

// 1. Swagger 설정 어노테이션 방식
@OpenAPIDefinition(
        info = @Info(title = "REST API 사용설명서",
        description = "REST API 수업 자료 입니다.",
        version = "v1",
        contact = @Contact(
                name = "bear",
                email = "bear.ohgiraffers@gmail.com"
        ))
)
@Configuration
public class SwaggerConfig {
}
