package section02;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import section02.common.NotificationClient;

/*
* @Bean으로 할 수 있는 것: 만드는 방법을 정하기
*  - 생성자에 넣을 값 정하기: URL, 키, 타임아웃을 원하는 값으로 넣을 수 있어요
*  - 만든 뒤 설정 메서드 호출하기: 외부 클래스에 setter 같은 게 있다면 호출할 수 있어요.
*  - 어떤 구현체를 쓸지 고르기: 반환 타입이 NotificationClient라서, 나중에 SmsNotificationClient로 바꿔도 이 메서드만 고치면 돼요. 이 빈을 쓰는 쪽은 그대로예요.
   - 이름 정하기, 같은 클래스로 빈 여러 개 만들기:
        @Bean public NotificationClient fastClient() { return new EmailNotificationClient("url", "key", 1); }
        @Bean public NotificationClient slowClient() { return new EmailNotificationClient("url", "key", 30); }
* */

public class Application {
    public static void main(String[] args) {
        /*
         * 등록된 빈 이름을 출력한 뒤, NotificationClient 빈을 꺼내서 send()를 호출해요
         * */
        ApplicationContext context = new AnnotationConfigApplicationContext(ContextConfiguration.class);

        // 등록된 bean 이름조회
        String[] beanNames = context.getBeanDefinitionNames();

        for (String bean : beanNames) {
            System.out.println(bean);
        }

        NotificationClient notificationClient = context.getBean(NotificationClient.class);

        System.out.println(notificationClient.send("hello@gmail.com", "가입 환영!"));
    }
}

