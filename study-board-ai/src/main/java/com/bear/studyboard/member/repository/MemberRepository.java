package com.bear.studyboard.member.repository;

import com.bear.studyboard.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long memberId);

    /*
     * 값이 있을을수도있고 없을수도있음
     * */
    Optional<Member> findByEmail(String email);
}
