package com.study.studyboard.member.repository;

import com.study.studyboard.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // Member 테이블에
    // 전달받은 이메일이 저장되어있으면 true 없으면 false
     boolean existsByEmail(String email);

     // Optional : 있을 수도, 없을 수도 있는 객체 상태
     Optional<Member> findByEmail(String email);
};