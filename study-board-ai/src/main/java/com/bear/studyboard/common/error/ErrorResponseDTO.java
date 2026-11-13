package com.bear.studyboard.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorResponseDTO {

    private String code;
    private String detail;

    static ErrorResponseDTO of(ErrorCode errorCode, String detail) {
        return new ErrorResponseDTO(errorCode.name(), detail);
    }
}
