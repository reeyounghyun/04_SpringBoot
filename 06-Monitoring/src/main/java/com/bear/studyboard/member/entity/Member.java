package com.bear.studyboard.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    // 비밀번호 비교에 사용하는 passwordHash()만 정의한다.
    @Column(name = "password_hash", nullable = false)
    @Getter(AccessLevel.NONE)
    private String passwordHash;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    @Column(name = "withdrawn_at")
    private LocalDateTime withdrawnAt;

    public Member(String email, String passwordHash, String nickname, LocalDateTime joinedAt) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.nickname = nickname;
        this.joinedAt = joinedAt;
    }

    public String passwordHash() {
        return passwordHash;
    }

    public void updateProfile(String email, String nickname) {
        if (email != null) {
            this.email = email;
        }
        if (nickname != null) {
            this.nickname = nickname;
        }
    }

    public void withdraw(LocalDateTime now) {
        this.withdrawnAt = now;
    }

    public boolean isWithdrawn() {
        return withdrawnAt != null;
    }
}
