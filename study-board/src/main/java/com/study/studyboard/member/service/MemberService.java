package com.study.studyboard.member.service;

import com.study.studyboard.common.error.BusinessException;
import com.study.studyboard.common.error.ErrorCode;
import com.study.studyboard.member.dto.*;
import com.study.studyboard.member.entity.Member;
import com.study.studyboard.member.repository.MemberRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    // 회원가입 - email은 unique(중복가입 x)
    public MemberAccountResponseDTO register(RegisterMemberRequestDTO request) {

        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.MEMBER_EMAIL_DUPLICATE);
        }

        // password 암호화
        // 사용자가 비밀번호를 만들면 동일한 비밀번호가 있을 수 있으면 무작위 솔트를 만들어 암호화 작업해줌
        // Bcrypt: 무작위 솔트를 만들고, 솔트(salt)와 입력 비밀번호로 해시를 계산해줌.
        // 같은 비밀번호로 가입해도 솔트가 다르면 해시가 달라짐 -> 반환문자열에 솔트와 해시가 함께 들어감(DB에 저장됨)
        String passwordHash = encodeNewPassword(request.getPassword());
        System.out.println("passwordHash = " + passwordHash);

        // Entity 객체 생성
        Member member = new Member(request.getEmail(), passwordHash, request.getNickname(), LocalDateTime.now());

        // 저장된 객체:영속상태가 됨.
        Member savedMember = memberRepository.saveAndFlush(member);

        return MemberAccountResponseDTO.from(savedMember);

    }

    // 패스워드를 인코딩하는 메서드
    public String encodeNewPassword(String password) {
        try {
            return passwordEncoder.encode(password);
        } catch (IllegalArgumentException exception) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "비밀번호는 UTF-8기준 72바이트 이하여야합니다.");
        }
    }

    // login 로직 , 로그인 메서드
    public MemberAccountResponseDTO login(LoginMemberRequestDTO request) {
        String email = request.getEmail();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.LOGIN_FAILED));

        // null이 아니면
        if (member.getDrawnAt() != null) {
            throw new BusinessException(ErrorCode.MEMBER_WITHDRAWN);
        }

        // matches(입력비밀번호, 저장된해시값)하면 저장된 해시의 salt값을 꺼내 입력된 비밀번호랑 비교
        if (request.getPassword() == null || !passwordEncoder.matches(request.getPassword(), member.passwordHash())) {
            //에러처리
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }

        return MemberAccountResponseDTO.from(member);
    }
    /*회원가입 end */

    // 회원 정보 수정
    @Transactional
    public MemberAccountResponseDTO updateMember(Long id, UpdateMemberRequestDTO request) {

        // 수정할 회원 찾아오기
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 이메일 중복 확인
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.MEMBER_EMAIL_DUPLICATE);
        }

        // 값 바꾸기
        member.updateProfile(request.getEmail(), request.getNickname());

        // 응답 DTO로 바꿔서 반환
        return MemberAccountResponseDTO.from(member);

    }

    // 회원 탈퇴
    @Transactional
    public MemberAccountResponseDTO withdrawMember(Long id, WithdrawMemberRequestDTO request) {
        // 회원인지 아닌지 확인
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 이미 탈퇴했으면
        if (member.getDrawnAt() != null){
            throw new BusinessException(ErrorCode.MEMBER_ALREADY_WITHDRAWN);
        }

        // 비밀번호가 틀리면
        if (!passwordEncoder.matches(request.getCurrentPassword(), member.passwordHash())){
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }

        member.withdraw();

        return MemberAccountResponseDTO.from(member);
    }

}
