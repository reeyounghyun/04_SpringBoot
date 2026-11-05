package org.example.restapi.section03.valid;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
* Validation (검증)
* - 클라이언트의 요청은 조작이 쉽고, 데이터가 비정상적으로 들어올 가능성이 있기에
* - 데이터 검증을 할 필요가 있다!
* */
@RestController
@RequestMapping("/valid")
public class ValidTestController { // 코드를 처리하는곳

    @PostMapping("/users") // @Valid @RequestBody UserDTO userDTO => @Valid앞에 작성해주면 검증하겠다 라는 의미
    public ResponseEntity<Void> saveUser(@Valid @RequestBody UserDTO userDTO) {

        System.out.println(userDTO);

        return ResponseEntity.ok().build();
    }
}

/*
* JSON 도착 → @Valid 보고 스프링이 UserDTO 규칙으로 검사
   ├─ 통과 → saveUser() 실행 → 200 OK
   └─ 실패 → 예외 발생 → 핸들러가 {"field":..., "message":...}로 400 응답
* */