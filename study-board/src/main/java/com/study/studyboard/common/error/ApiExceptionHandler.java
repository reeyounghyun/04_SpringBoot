package com.study.studyboard.common.error;

import com.study.studyboard.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


// 공통응답과 함께 (예외가 나와도 같은 형식으로 나오게)
@RestControllerAdvice(basePackages = "com.study.studyboard")
public class ApiExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<ErrorResponseDTO>> handleBusinessException(BusinessException exception) {

        return errorResponse(exception.getErrorCode(), exception.getMessage());
    }
    // 위에서 처리하지 못한 나머지 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ErrorResponseDTO>> handleException(Exception exception) {

        // 서버 내부의 정보가 응답에 노출될 가능성이 있음 -> 배포시에는 메시지를 null로 처리하는것이 필요함.
        return errorResponse(ErrorCode.INTERNAL_ERROR, exception.getMessage());
    }

    // 응답생성 메서드
    private ResponseEntity<ApiResponse<ErrorResponseDTO>> errorResponse(ErrorCode errorCode, String detail) {
        HttpStatus status = errorCode.getHttpStatus();
        ErrorResponseDTO error = ErrorResponseDTO.of(errorCode, detail);

        return ResponseEntity.status(status)
                .body(ApiResponse.of(status, errorCode.getMessage(), error));
    }
}
