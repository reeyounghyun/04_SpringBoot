package com.bear.studyboard.common.error;

import com.bear.studyboard.common.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

// 모든 컨트롤러에서 던져진 예외를 한곳에서 받아 공통 오류 응답(ApiResponse)으로 바꾼다.
@RestControllerAdvice(basePackages = "com.bear.studyboard")
@Slf4j
public class ApiExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<ErrorResponseDTO>> handleBusinessException(BusinessException exception) {
        return errorResponse(exception.getErrorCode(), exception.getMessage());
    }

    // 요청 body를 읽지 못했거나(JSON 형식 오류, 숫자 자리에 글자 등) 경로 값의 타입이 맞지 않으면 400으로 처리한다.
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiResponse<ErrorResponseDTO>> handleInvalidRequest(Exception exception) {
        return errorResponse(ErrorCode.INVALID_INPUT, "요청 값의 형식이 올바르지 않습니다.");
    }

    // 위에서 처리하지 못한 나머지 모든 예외를 500으로 처리한다. (마지막 안전망)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ErrorResponseDTO>> handleUnexpectedError(Exception exception) {
        // 예외 메시지에 SQL 값이 포함될 수 있으므로 예외 유형만 기록한다.
        log.error("Unexpected API error type={}", exception.getClass().getName());
        return errorResponse(ErrorCode.INTERNAL_ERROR, null);
    }

    // 세 핸들러가 함께 쓰는 응답 생성 메서드. ErrorCode로 HTTP 상태, message, data(code, detail)를 만든다.
    private ResponseEntity<ApiResponse<ErrorResponseDTO>> errorResponse(ErrorCode errorCode, String detail) {

        HttpStatus status = errorCode.getHttpStatus();

        ErrorResponseDTO error = ErrorResponseDTO.of(errorCode, detail);
        ApiResponse<ErrorResponseDTO> response = ApiResponse.of(status, errorCode.getMessage(), error);

        return ResponseEntity.status(status).body(response);
    }
}
