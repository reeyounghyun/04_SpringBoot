package section02.common;

import org.springframework.stereotype.Service;

// 생성자로 주입받아 쓰려고 만든 클래스
@Service
public class NotificationService {

    NotificationClient notificationClient;

    public NotificationService(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }
}
