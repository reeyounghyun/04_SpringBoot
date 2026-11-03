package section02;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import section02.common.EmailNotificationClient;
import section02.common.NotificationClient;

@Configuration
@ComponentScan("section02")
public class ContextConfiguration {

    /*
    EmailNotificationClient를
     @Bean 메서드에서 new로 직접 만들고 설정값을 넣어 줘요.
    * */
    @Bean
    public NotificationClient notificationClient(){

        return new EmailNotificationClient(
                "apiUrl",
                "api-key",
                3
        );
    }

}
