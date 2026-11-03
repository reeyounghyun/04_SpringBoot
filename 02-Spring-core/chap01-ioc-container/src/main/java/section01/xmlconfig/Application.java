package section01.xmlconfig;

import common.MemberDTO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class Application {

    // Application Context 를 만들것!
    // XML 파일로 설정정보를 담아서 만들어보기
    public static void main(String[] args) {

        // new로 GenericXmlApplicationContext 객체를 새로 만들고,
        // 그 객체를 ApplicationContext 타입 변수 context에 담는다.
        ApplicationContext context = new GenericXmlApplicationContext("xmlconfig/spirng-context.xml");

        // ID로 가져오는 방법
        // 객체를 직접 만들지 않음에도 ApplicationContext가 객체를 만들어서 가져와줌.
        MemberDTO member1 = (MemberDTO)context.getBean("member");
        System.out.println("member1 =" + member1);

        // 클래스의 메타정보를 이용해 가져오는 방법
        MemberDTO member2 = context.getBean(MemberDTO.class);
        System.out.println("member2 =" + member2);

        // 클래스의 메타정보와 이름을 이용해 가져오는 방법
        MemberDTO member3 = context.getBean("member", MemberDTO.class);
        System.out.println("member3 =" + member3);
    }
}
