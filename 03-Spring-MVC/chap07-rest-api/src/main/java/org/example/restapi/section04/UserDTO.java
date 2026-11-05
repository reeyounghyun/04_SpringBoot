package org.example.restapi.section04;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserDTO {
    // 안에 들어가는 실제 데이터 no/id/pwd/name/enrollAt
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

