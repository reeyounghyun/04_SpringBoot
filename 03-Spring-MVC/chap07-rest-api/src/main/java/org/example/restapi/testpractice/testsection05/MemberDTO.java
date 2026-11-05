package org.example.restapi.testpractice.testsection05;

import lombok.*;

import java.time.LocalDate;

/*
 * [보너스 1] 각 필드에 @Schema(description = "...", example = "...")를 붙여 보세요.
 * */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MemberDTO {

    private int no;             // 회원 번호
    private String id;          // 아이디
    private String name;        // 이름
    private String email;       // 이메일
    private LocalDate joinedAt; // 가입일
}
