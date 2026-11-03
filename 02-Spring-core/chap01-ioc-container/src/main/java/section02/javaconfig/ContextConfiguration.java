package section02.javaconfig;

import common.MemberDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/*@Configuration
* 정의: 클래스가 설정파일을 의미
* 이 클래스가 spirng-context.xml 같은 설정 파일 역할을 한다*/
@Configuration
public class ContextConfiguration {

    /* 직접 빈을 등록해야하는 경우만 JAVA Config 방식을 사용함.
    * - 1.외부 라이브러리를 사용할때 : 외부 라이브러리에 이노테이션을 붙일 수 없음.
    * - 2.객체를 꼭 초기화해야 할 때 : 설정값을 넣어서 Bean으로 등록함.
    * */
    @Bean(name = "member")
    public MemberDTO getMember() {
        return new MemberDTO(1, "user01", "pass01", "bear");
    }
}
