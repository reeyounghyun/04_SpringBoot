package com.bear.studyboard.member.service;

import com.bear.studyboard.common.error.BusinessException;
import com.bear.studyboard.common.error.ErrorCode;
import com.bear.studyboard.member.dto.LoginMemberRequestDTO;
import com.bear.studyboard.member.dto.MemberAccountResponseDTO;
import com.bear.studyboard.member.dto.RegisterMemberRequestDTO;
import com.bear.studyboard.member.dto.UpdateMemberRequestDTO;
import com.bear.studyboard.member.entity.Member;
import com.bear.studyboard.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberAccountResponseDTO register(RegisterMemberRequestDTO request) {
        String email = request.getEmail();
        String nickname = request.getNickname();
        if (memberRepository.existsByEmail(email)) {
            throw new BusinessException(ErrorCode.MEMBER_EMAIL_DUPLICATE);
        }

        // BCrypt는 무작위 솔트를 만들고, 그 솔트와 입력 비밀번호로 해시를 계산한다.
        // 같은 비밀번호로 가입해도 솔트가 다르면 해시가 달라진다.
        String passwordHash = passwordEncoder.encode(request.getPassword());
        Member member = new Member(email, passwordHash, nickname, currentDateTime());
        Member savedMember = memberRepository.save(member);
        return MemberAccountResponseDTO.from(savedMember);
    }

    @Transactional(readOnly = true)
    public MemberAccountResponseDTO login(LoginMemberRequestDTO request) {
        String email = request.getEmail();
        String password = request.getPassword();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.LOGIN_FAILED));

        if (!matchesPassword(password, member.passwordHash())) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }

        // 비밀번호가 맞아도 이미 탈퇴한 회원은 사용할 수 없게 막는다.
        if (member.isWithdrawn()) {
            throw new BusinessException(ErrorCode.MEMBER_WITHDRAWN);
        }

        // 여기서는 세션이나 토큰을 만들지 않고, 비교에 성공한 회원 정보만 반환한다.
        return MemberAccountResponseDTO.from(member);
    }

    @Transactional
    public MemberAccountResponseDTO updateMember(long memberId, UpdateMemberRequestDTO request) {
        String email = request.getEmail();
        String nickname = request.getNickname();

        Member member = verifyActiveMemberPassword(memberId, request.getCurrentPassword());
        // 변경할 이메일을 현재 회원이 아닌 다른 회원이 사용 중인지 확인한다.
        if (email != null && memberRepository.existsByEmailAndIdNot(email, memberId)) {
            throw new BusinessException(ErrorCode.MEMBER_EMAIL_DUPLICATE);
        }

        // 수정
        member.updateProfile(email, nickname);

        return MemberAccountResponseDTO.from(member);
    }

    @Transactional
    public MemberAccountResponseDTO withdrawMember(long memberId, String currentPassword) {

        Member member = verifyActiveMemberPassword(memberId, currentPassword);

        member.withdraw(currentDateTime()); // 삭제날짜 작성

        return MemberAccountResponseDTO.from(member);
    }

    // 회원이 존재하고 탈퇴하지 않은 활성 회원인지 검증한다.
    // 이 회원이 지금 활동할 수 있는 회원인지 두 단계로 확인한다
    public Member requireActiveMember(long memberId) {

        // 존재하는지 검증
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        // 삭제되었는지 검증
        if (member.isWithdrawn()) {
            throw new BusinessException(ErrorCode.MEMBER_WITHDRAWN);
        }

        return member;
    }

    // 활성 회원인지 확인한 뒤 입력한 현재 비밀번호가 일치하는지 검증한다.
    private Member verifyActiveMemberPassword(long memberId, String currentPassword) {
        Member member = requireActiveMember(memberId);

        if (!matchesPassword(currentPassword, member.passwordHash())) {
            throw new BusinessException(ErrorCode.INVALID_MEMBER_PASSWORD);
        }
        return member;
    }

    // 입력한 비밀번호가 저장된 해시와 일치하는지 확인한다.
    private boolean matchesPassword(String password, String passwordHash) {

        if (password == null) {
            return false;
        }
        return passwordEncoder.matches(password, passwordHash);
    }


    private static LocalDateTime currentDateTime() {
        return LocalDateTime.now();
    }
}
