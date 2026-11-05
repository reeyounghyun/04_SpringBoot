package org.example.restapi.section03.exceptionenum;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/*
* 비즈니스로직의 예외들을 처리해주는 예외처리 클레스
* */
@RestController
@RequestMapping("/enum-errors")
public class EnumExceptionController {

    private final Map<Integer, String> users = Map.of(
            1, "홍길동",
            2, "유관순"
    );

    // 회원 번호로 회원 조회
    @GetMapping("/users/{userNo}")
    public ResponseEntity<Map<String, Object>> findUser(@PathVariable int userNo) {

        // 회원 번호가 1보다 작으면 잘못된 요청 → INVALID_USER_NUMBER(400) 예외 발생
        if (userNo < 1) {
            throw new BusinessException(ErrorCode.INVALID_USER_NUMBER);
        }

        // users.get의 get은 Map에 get(키) = Users No(숫자) 매서드 이다.
        String userName = users.get(userNo); //예외가 발생하지 않았을 때 실행됩

        if (userName == null) {
            throw new BusinessException("회원번호 " + userNo + "에 해당하는 회원이 없습니다.", ErrorCode.USER_NOT_FOUND);
        }

        // 검증을 통과하면 200 OK 응답 (본문 없음)
        return ResponseEntity.ok().build();
    }

    // 회원 이름 중복 확인
    @GetMapping("/users/check-name")
    public ResponseEntity<Map<String, Object>> check(@RequestParam String userName) {

        // 이미 등록된 이름이면 → DUPLICATE_USER_NAME(409) 예외 발생
        if (users.containsValue(userName)) {
            throw new BusinessException(userName + "은(는) 이미 등록된 이름입니다.", ErrorCode.DUPLICATE_USER_NAME);
        }

        // 중복이 없으면 200 OK 응답 (본문 없음)
        return ResponseEntity.ok().build();
    }
}
