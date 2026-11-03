package demo.repository;

import org.springframework.stereotype.Repository;

/**
 * 데이터 접근 계층(Repository).
 * <p>
 * 실제로는 DB에서 데이터를 조회·저장하는 역할을 담당한다.
 * {@code @Repository}를 붙이면 컴포넌트 스캔 대상이 되어 스프링 빈으로 등록된다.
 */
@Repository
public class DemoRepository {

    /**
     * 메시지를 조회한다.
     * 지금은 DB 대신 고정된 문자열을 반환한다.
     */
    public String findMessage() {
        return "Hello from DemoRepository";
    }
}
