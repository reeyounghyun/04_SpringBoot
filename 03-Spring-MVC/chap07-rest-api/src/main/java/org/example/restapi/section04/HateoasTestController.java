package org.example.restapi.section04;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Hateoas (Hypermedia As The Engine Of Application State)
 * -REST API 응답에 현재 데이터 뿐 아니라, 해당 데이터와 관련된 다음 요청의 링크도 함께 제공하자.
 * */
@RestController
@RequestMapping("/hateoas")
public class HateoasTestController {

    private List<UserDTO> users;
    private final DtoModelAssembler assembler;


    /*
     * 동작 방식(생성자 주입): DtoModelAssembler에는 @Component가 붙어 있어서 Spring이 객체를 미리 만들어 둡니다.
     * 컨트롤러를 만들 때 Spring이 그 객체를 생성자에 넣어 주고, 코드는 그걸 this.assembler에 저장합니다.
     * */
    public HateoasTestController(DtoModelAssembler assembler) {
        this.assembler = assembler;
        this.users = new ArrayList<>();

        users.add(new UserDTO(1, "user01", "pass01", "홍길동", new Date()));
        users.add(new UserDTO(2, "user02", "pass02", "유관순", new Date()));
        users.add(new UserDTO(3, "user03", "pass03", "이순신", new Date()));
    }


    // 유저 단건 조회 -> 회원 번호(userNo)로 한 명의 유저 정보를 조회
    // DtoModelAssembler의 self 링크(/hateoas/user/{userNo})가 이 메서드를 가리킴
    @GetMapping("/user/{userNo}")
    public ResponseEntity<ResponseMessage> findUserByUseNo(@PathVariable int userNo) {
        /*
         * application/json : HTTP의 응답이 JSON 형식의 데이터임을 의미
         * */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                new MediaType("application", "json", StandardCharsets.UTF_8)
        );

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

        // 메서드 체이닝 + 빌더 패턴
        return ResponseEntity
                .ok() // 200 상태코드
                .headers(headers)
                .body(new ResponseMessage(200, "조회성공", responseMap));
    }

    // 유저 목록 전체 조회
    // 각각의 유저 정보가 전달되는데, 해당 정보를 단건조회할수있게 링크를 제공
    @GetMapping("/users")
    public ResponseEntity<ResponseMessage> findAllUsers() {

        // 사용자 목록을 EntityModel로 변환하기위해 Assembler의 toModel 메소드 사용
        List<EntityModel<UserDTO>> usersWithRel = users.stream()
                .map(user -> assembler.toModel(user))
//                .map(assembler::toModel) // 같은내용
                .toList();

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("users", usersWithRel);


        ResponseMessage responseMessage = new ResponseMessage(200, "조회 성공", responseMap);

        /*
         * application/json : HTTP의 응답이 JSON 형식의 데이터임을 의미
         * */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                new MediaType("application", "json", StandardCharsets.UTF_8)
        );

        // ResponseEntity로 반환 (body, headers, status(상태코드))
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

}