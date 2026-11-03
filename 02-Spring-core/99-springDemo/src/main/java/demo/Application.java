package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 스프링 부트 애플리케이션의 시작점.
 * <p>
 * {@code @SpringBootApplication}은 이 클래스가 위치한 {@code demo} 패키지와 그 하위 패키지
 * (controller, service, repository)를 컴포넌트 스캔하여 빈으로 등록한다.
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        // 스프링 컨테이너를 생성하고 내장 톰캣 서버를 실행한다.
        SpringApplication.run(Application.class, args);
    }

}
