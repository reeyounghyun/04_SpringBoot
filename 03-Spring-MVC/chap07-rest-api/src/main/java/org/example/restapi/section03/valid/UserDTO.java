package org.example.restapi.section03.valid;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;

/*
* Spring Boot Validation
* - 사용자가 보낸 데이터가 정해진 조건을 만족하는지 검사하는 라이브러리
* */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserDTO { // 규칙표

    private int no;

    /*
    * @NotNull  : null이면 안 됨 (값 자체가 없는 경우 검사)
    * @NotBlank : null, 빈 문자열(""), 공백(" ")이면 안 됨 (문자열 전용)
    * @Size     : 문자열 길이가 min 이상, max 이하인지 검사
    * @Pattern  : 정규식(regexp)과 일치하는지 검사
    *   - ^[a-zA-Z0-9_]+$ : 영문 대소문자, 숫자, 밑줄(_)만 허용
    *   - ^ : 문자열 시작 / $ : 문자열 끝 / + : 1글자 이상
    * */

    // Validation: 순서 처리하고싶은 순서대로 위에다 배치해주면된다.
    @NotNull(message = "아이디는 반드시 입력해야합니다.")
    @NotBlank(message = "아이디는 공백일 수 없습니다.")
    @Size(min = 8, max=20, message = "아이디는 8자이상, 20자 이하로 입력해야합니다.")
    // 정규표현식(regexp)
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "아이디는 영문자, 숫자, 밑줄(_)만 허용됩니다.")
    private String id;

    // 패스워드는 8자 이상 30자 이하로 입력해야합니다. 그리고 대문자,소문자,숫자,ㅌㄱ수문자가 각각 하나 이상 포함되어야 한다.
    @Size(min=8, max=30, message="패스워드는 8자 이상 30자 이하로 입력해야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,30}$", message="패스워드는 대문자, 소문자, 숫자, 특수문자 각각 하나 이상 포함되어야 합니다.")
    private String pwd;

    // 이름은 반드시 입력해야한다, 조건 2글자 이상 5글자 이하
    @NotBlank(message="이름은 반드시 입력해야한다.")
    @Size(min=2, max=5, message="이름은 2글자 이상 5글자 이하로 입력해야합니다.")
    private String name;

    @Past(message="가입일은 과거 날짜만 입력할 수 있습니다.")
    /*
    @PastOrPresent(message="과거 또는 현재.")
    @Future(message="현재보다 미래.")
    @FutureOrPresent(message="현재 또는 미래.")
    */
    private Date enrollAt;

    public UserDTO(String id, String pwd, String name) {
        this.id = id;
        this.pwd = pwd;
        this.name = name;
    }
}

