package section03.annotationconfig;

import common.MemberDTO;
import common.TestDTO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

    // Application Context 를 만들것!
    // 어노테이션(@ComponentScan)으로 설정정보를 담아서 만들어보기
    public static void main(String[] args) {

        ApplicationContext context =
                new AnnotationConfigApplicationContext(ContextConfiguration.class);

        // 클래스의 메타정보와 이름을 이용해 가져오는 방법
//      TestDTO TestDTO = context.getBean(TestDTO.class);
//        System.out.println("TestDTO =" + TestDTO);

        // 스프링컨테이너에 생성된 bean들의 이름을 배열로 반환
        String[] beanNames = context.getBeanDefinitionNames();

        for (String beanName : beanNames) {
            System.out.println("beanName = " + beanName);
        }

    }
}
