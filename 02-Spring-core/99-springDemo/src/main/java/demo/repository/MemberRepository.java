package demo.repository;

import org.springframework.stereotype.Repository;

/**
 * 회원 데이터 접근 계층(Repository).
 * <p>
 * 실제로는 DB에서 회원 정보를 조회·저장하는 역할을 담당한다.
 * {@code @Repository}를 붙이면 컴포넌트 스캔 대상이 되어 스프링 빈으로 등록된다.
 */
@Repository
public class MemberRepository {
}
