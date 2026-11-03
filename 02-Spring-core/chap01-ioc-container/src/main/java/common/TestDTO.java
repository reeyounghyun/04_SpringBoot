package common;

import org.springframework.stereotype.Component;


/*
@Component
* 이노테이션이 붙으면 @ComponentScan을 할때 자동으로 스프링컨텍스트에 Bean 등록된다. */
//@Component 스캔 테스트용 클래스
@Component
public class TestDTO {
    private int no;
}
