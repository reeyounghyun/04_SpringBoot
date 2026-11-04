package org.example.restapi.section03.valid;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class UserDTO {

    @NotNull(message = "아이디는 반드시 입력해야 합니다.")
    @NotBlank(message = "아이디는 공백일 수 없습니다.")
    private int no;
    private String id;
    private String pwd;
    private String name;
    private Date enrollAt;

    public UserDTO(String id, String pwd, String name) {
        this.id = id;
        this.pwd = pwd;
        this.name = name;
    }
}

