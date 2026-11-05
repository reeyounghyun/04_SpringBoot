package org.example.restapi.testpractice.testsection05;

import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MemberResponseMessage {

    private int httpStatus;              // HTTP 응답코드
    private String message;              // 응답 메시지
    private Map<String, Object> result;  // 응답 데이터
}
