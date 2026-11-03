package org.example.restapi.section02.responseentity;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/entity")
public class ResponseEntityController {

    private List<UserDTO> users;

    public ResponseEntityController() {
        this.users = new ArrayList<>();  // 초기화

        users.add(new UserDTO(1, "user01", "pass01", "홍길동", new Date()));
        users.add(new UserDTO(2, "user02", "pass02", "유관순", new Date()));
        users.add(new UserDTO(3, "user03", "pass03", "이순신", new Date()));
    }

    // 유저 목록 전체 조회
    @GetMapping("/users")
    public ResponseEntity<ResponseMessage> findAllUsers() {

        /*
         * application/json : HTTP의 응답이 JSON 형식의 데이터임을 의미
         * */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                new MediaType("application", "json", StandardCharsets.UTF_8)
        );

        // 응답 데이터 담기
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("users", users);

        // 응답 메시지 객체 생성
        ResponseMessage responseMessage = new ResponseMessage(200, "조회성공", responseMap);

        // ResponseEntity로 반환 (body, headers, status(상태코드))
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    // 특정 유저 상세 조회
    @GetMapping("/users/{userNo}")
    public ResponseEntity<ResponseMessage> findUserByNo(@PathVariable int userNo) {

        /*
         * application/json : HTTP의 응답이 JSON 형식의 데이터임을 의미
         * */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                new MediaType("application", "json", StandardCharsets.UTF_8)
        );

        // userNo와 일치하는 유저 찾기
        UserDTO foundUser = new UserDTO();

        // 일치하는 회원 찾기
        for (UserDTO user : users) {
            if (user.getNo() == userNo) {
                foundUser = user;
                break;
            }
        }

        // 응답 데이터
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("user", foundUser);

        // 매서드 제이닝 + 빌터 패턴
        return  ResponseEntity
                .ok() // 200 상태코드
                .headers(headers)
                .body(new ResponseMessage(200, "조회성공", responseMap));
    }


    /* @RequestBody
    *  HTTP 요청의 본문에 담긴 데이터를 JAVA 객체로 변환하여 매개변수로 전달
    * */

    // 신규 유저 등록 => 등록된 유저를 조회할 수 있는 API 주소를 응답 헤더(Location)에 담아 반환
    @PostMapping("/users")
    public ResponseEntity<Void> registUser(@RequestBody UserDTO newUser) {

        System.out.println("newUser = " + newUser);

        // sers.size() - 1 크기에 마직 회의 회원번호
        int lastUserNo = users.get(users.size() - 1).getNo(); // 마직 회원의 회원번호
        int newUserNo = lastUserNo + 1;

        newUser.setNo(newUserNo);
        newUser.setEnrollAt(new Date());

        // 저장
        users.add(newUser);

       // return ResponseEntity.ok().build();

        return ResponseEntity
                .created(URI.create("/entity/users/" + newUserNo))  // newUserNo 응답할때 추가됨
                .build();
    }

    // 기존 유저 정보를 수정



    // 기존 유저 정보를 삭제
    @DeleteMapping("/users/{userNo}")
    public ResponseEntity<?> removeUser(@PathVariable int userNo) {

        // userNo와 일치하는 회원 찾기
        UserDTO foundUser = users.stream()
                .filter(user -> user.getNo() == userNo)
                .toList().get(0);

        // 목록에서 삭제
        users.remove(foundUser);

        // 204 No Content : 요청은 성공했지만 응답 본문은 없음
        return ResponseEntity
                .noContent()
                .build();
    }

    // 특정 회원의 정보를 전체 수정
    @PutMapping("/users/{userNo}")
    public ResponseEntity<Void> PutEntity(@PathVariable int userNo, @RequestBody UserDTO newUser) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                new MediaType("application", "json", StandardCharsets.UTF_8)
        );

        // id와 pwd가 모두 일치하는 회원 찾기 (문자열 비교는 == 말고 equals 사용)
        UserDTO foundUser = users.stream()
                .filter(user -> user.getId().equals(newUser.getId())
                             && user.getPwd().equals(newUser.getPwd()))
                .toList().get(0);

        // 찾은 회원의 정보 수정
        foundUser.setName(newUser.getName());
        foundUser.setEnrollAt(new Date());



        return ResponseEntity.ok().headers(headers).build();
    }

}
