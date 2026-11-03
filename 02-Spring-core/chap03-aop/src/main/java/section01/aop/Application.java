package section01.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ContextConfiguration.class);

        MemberService memberService = context.getBean(MemberService.class);

        System.out.println("===== TARGET 실행");
        //System.out.println(memberService.processMember(false));

        // within 예제
        //memberService.selectMember();

        // this 예제
        // memberService.checkMember();

        // args 예제
        // memberService.registerMember("홍길동", 20);


        memberService.updateMember();
    }
}
