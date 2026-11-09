package org.example.springdatajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/*
 * 스캔 범위 확장 (연습문제 폴더 포함)
 *
 * [왜 필요한가?]
 * - @SpringBootApplication은 기본적으로 "자기가 있는 패키지(org.example.springdatajpa)와 그 하위"만 스캔한다.
 * - 연습문제는 형제 폴더(org.example.exercise)에 있어서 기본 설정으로는 스캔되지 않는다.
 *   → Controller, Service, Repository가 빈(Bean)으로 등록되지 않아 API 호출 시 404가 난다.
 * - 그래서 공통 상위 패키지인 org.example로 스캔 범위를 넓힌다.
 *
 *   org.example
 *   ├── springdatajpa   (강의 코드)
 *   └── exercise        (연습문제)
 *
 * [각 설정의 역할]
 * - scanBasePackages = "org.example"
 *     @Controller, @Service, @Component 등이 붙은 클래스를 찾아 빈으로 등록하는 범위
 * - @EnableJpaRepositories(basePackages = "org.example")
 *     JpaRepository를 상속한 인터페이스를 찾아 구현체(Repository 빈)를 만들어주는 범위
 *     (Repository는 일반 빈 스캔과 별도로 동작하므로 따로 지정해야 한다)
 *
 * [@EntityScan은 왜 없나?]
 * - 엔티티(Menu, Category)는 강의 패키지에만 있고, 연습문제는 그 엔티티를 import해서 사용한다.
 * - 기본 엔티티 스캔 범위(org.example.springdatajpa)로 충분하므로 추가하지 않았다.
 */
@SpringBootApplication(scanBasePackages = "org.example")
@EnableJpaRepositories(basePackages = "org.example")
public class Chap05SpringDataJpaApplication {
    public static void main(String[] args) {
        SpringApplication.run(Chap05SpringDataJpaApplication.class, args);
    }
}
