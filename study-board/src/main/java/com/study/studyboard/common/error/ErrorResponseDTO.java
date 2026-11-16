package com.study.studyboard.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ErrorResponseDTO {

    private String code;
    private String detail;

    static ErrorResponseDTO of(ErrorCode errorCode, String detail) {
        return new ErrorResponseDTO(
                errorCode.name(), detail
        );
    }

}
