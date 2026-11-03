package demo.service;

import demo.repository.DemoRepository;
import demo.repository.MemberRepository;
import org.springframework.stereotype.Service;

/**
 * 비즈니스 로직 계층(Service).
 * <p>
 * Controller와 Repository 사이에서 실제 업무 처리를 담당한다.
 * {@code @Service}를 붙이면 컴포넌트 스캔 대상이 되어 스프링 빈으로 등록된다.
 */
@Service
public class DemoService {

    // 생성자 주입으로 받은 의존성은 변경되지 않도록 final로 선언한다.
    private final MemberRepository memberRepository;
    private final DemoRepository demoRepository;

    /**
     * 생성자 주입.
     * 생성자가 하나뿐이면 {@code @Autowired}를 생략해도 스프링이 자동으로 MemberRepository, DemoRepository 빈을 주입한다.
     */
    public DemoService(MemberRepository memberRepository, DemoRepository demoRepository) {
        this.memberRepository = memberRepository;
        this.demoRepository = demoRepository;
    }

    /**
     * Repository에서 메시지를 조회해 반환한다.
     */
    public String getMessage() {
        return demoRepository.findMessage();
    }
}
