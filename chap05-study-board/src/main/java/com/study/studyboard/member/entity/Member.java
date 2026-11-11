package com.study.studyboard.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity // 영속성 객체이기 때문에 Setter를 붙이지 않음.
@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email; // 이메일 중복가입이 안된다.(내가 설정가능)

    @Column(name = "password_hash", nullable = false)
    @Getter(AccessLevel.NONE) //getter 접근이 안되게 설정함
    private String passwordHash; // 게터로 조회할수있다는건 상시 확인할수 있다는것???

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinAt; // 언제 가입했는지

    //회원탈퇴 시각
    @Column(name = "withdrawn_at")
    private LocalDateTime drawnAt;

    public void withdraw() {
        this.drawnAt = LocalDateTime.now();
    }

    // 처음 회원가입 할 때 사용할 생성자 등
    public Member(String email, String passwordHash, String nickname, LocalDateTime joinAt) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.nickname = nickname;
        this.joinAt = joinAt;
    }

    // getter 접근 안 되게 메서드 만들기
    // 패스워드만 조회가능한 메서드
    public String passwordHash() {
        return passwordHash;
    }

    // Entity 수정은 setter을 사용하지 않고 수정하는 내용을 명시할 수 있는 메서드를 따로 만들어준다.
    public void updateProfile(String email, String nickname) {
        if(email != null) {
            this.email = email;
        }
        if(nickname != null)  {
            this.nickname = nickname;
        }
    }


}
