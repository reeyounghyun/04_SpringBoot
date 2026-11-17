package com.bear.studyboard.common.error;

import com.bear.studyboard.common.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.generic.LocalVariableGen;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import jakarta.servlet.http.HttpServletRequest;

// 모든 컨트롤러에서 던져진 예외를 한곳에서 받아 공통 오류 응답(ApiResponse)으로 바꾼다.
@RestControllerAdvice(basePackages = "com.bear.studyboard")

// SLF4J 객체를 통해 로그를 작성 할 수 있게 해주는 어노테이션이다.
// lombok에서 제공하는 log를 쓸수 있게해주는 어노테이션
@Slf4j

public class ApiExceptionHandler {

    // BusinessException 클레스로, 우리가 만든 예외처리 클레스이다.
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<ErrorResponseDTO>> handleBusinessException(
            BusinessException exception, HttpServletRequest request) {

        /*
        HttpServerRequest 스프링이 주입을 해준다.
        {} 자리에는 뒤에 작은 값이 순서대로 들어간다.
        */
        log.warn("요청처리실패 - {} {}, code = {}",
                request.getMethod(),
                request.getRequestURI(),
                exception.getErrorCode());


        return errorResponse(exception.getErrorCode(), exception.getMessage());
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
