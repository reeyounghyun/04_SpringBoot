package com.study.studyboard.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password4j.BcryptPassword4jPasswordEncoder;

@Configuration
//모든 패키지의 기능들이 공통으로 사용
// PasswordConfig : 앱이 시작될 때 한 번 세팅
public class PasswordConfig {
    // 서비스에서 주입받을 수 있도록 PasswordEncoder를 빈으로 등록함
    // → MemberService의 private final PasswordEncoder passwordEncoder; 필드에 생성자 주입됨
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BcryptPassword4jPasswordEncoder();
    }

}

