package org.example.restapi.section03.valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice(basePackages = "org.example.restapi.section03.valid")
public class ValidExceptionHandler { //  이미 발생한 예외를 잡아서 처리하는 코드 && 반려 담당자

    // MethodArgumentNotValidException.class : Validation 발생시키는 예외
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(
            MethodArgumentNotValidException exception) {

        // FieldError : 특정 필드에서 발생한 검증오류정보를 담은 객체
        FieldError fieldError = exception.getBindingResult().getFieldError();

        Map<String, String> response = Map.of(
                "field", fieldError.getField(),  // 검증에 실패한 필드 이름 (예: "id")
                "message", fieldError.getDefaultMessage()   // UserDTO 어노테이션에 작성한 message 문구
        );

        return ResponseEntity.badRequest().body(response);
    }
}
