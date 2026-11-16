package com.study.studyboard.common.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

// ApiResponse: 컨트롤러들이 가져다 쓰는 도구
public class ApiResponse<T> {

    private int status;
    private String message;
    private T data;

    /*
    * JAVA의 정적 팩토리 메서드 (관례)
    * from
    *  : 매개변수를 하나를 받아 객체를 생성할 때 사용한다.
    * of
    * : 여러 값을 조합해서 객체를 생성할 때 사용한다.
    * */
    public static <T> ApiResponse<T> of(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status.value(), message, data);
    }
}
