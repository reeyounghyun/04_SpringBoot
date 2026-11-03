package section04;

import common.MemberDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration("ConfigurationSection04")
public class ContextConfiguration {

    /*
     * @Bean의 기본 Scope
     * - singleton : 싱글톤
     * - 정해진 범위 안에서 객체의 인스턴스를 하나만 생성하고 해당 객체를
     * - 필요로하는 곳들에서는 생성된 인스턴스를 재사용하게끔하는개념
     * */
    @Bean(name = "member")
    public MemberDTO getMember() {
        return new MemberDTO(1, "user01", "pass01", "bear");
    }
}
