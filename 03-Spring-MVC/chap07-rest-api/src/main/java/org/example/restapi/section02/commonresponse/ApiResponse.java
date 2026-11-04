package org.example.restapi.section02.commonresponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*
모든 API에서 공통으로 사용할 응답 형식
 @param <T> 응답 본문에 담을 데이터 타입
*
*/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse <T> {
    private int status;
    private String message;
    private T data;

    public ApiResponse(int status) {
        this.status = status;
    }

    /*
    * 팩토리 메서드
    * - 객체 생성과정을 캡슐화하여 객체를 생성,반환하는 메서드
    * - 생성 목적을 이름으로 명확하게 표현
    *
    * ApiResponse.success( 이런식으로 호출)
    * */
    public static <T> ApiResponse<T> of(int status) {
        return new ApiResponse<>(status);
    }

    public static <T> ApiResponse<T> success(int status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }

    public static <T> ApiResponse<T> failure(int status, String message, T data) {
        return new ApiResponse<>(status, message, null);
    }

}
