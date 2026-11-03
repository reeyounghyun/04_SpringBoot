package section02.javaconfig;

import common.MemberDTO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

    // Application Context 를 만들것!
    // XML 파일로 설정정보를 담아서 만들어보기
    public static void main(String[] args) {

        ApplicationContext context =
                new AnnotationConfigApplicationContext(ContextConfiguration.class);

        // 객체를 직접 만들지 않았는데도  ApplicationContext가 객체를 만들어서 가져와줌 // new만들지 않아도
        MemberDTO member1 = (MemberDTO) context.getBean("member");
        System.out.println("member1 =" + member1);

        // 클래스의 메타정보를 이용해 가져오는 방법
        MemberDTO member2 = context.getBean(MemberDTO.class);
        System.out.println("member2 =" + member2);

        // 클래스의 메타정보와 이름을 이용해 가져오는 방법
        MemberDTO member3 = context.getBean("member", MemberDTO.class);
        System.out.println("member3 =" + member3);
    }
}
