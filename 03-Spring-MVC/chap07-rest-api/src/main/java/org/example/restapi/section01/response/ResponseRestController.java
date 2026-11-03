package org.example.restapi.section01.response;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * @ResponseBody
 * - 반환값이 응답 본문(Body)에 직접 쓰여짐 (View를 거치지 않는다)
 * - 주로 JSON, XML 같은 형식의 데이터를 HTTP로 반환하고자 할때 사용한다.
 * @Controller + @ResponseBody => @RestController
 */
/*
 * JSON : JavaScript Object Notation
 * - 자바스크립트가 인지할 수 있는 객체형태의 문자열
 * 배열 -> [] 형태로 표기 (리스트)
 * 객체 -> {} 형태로 표기 (클래스타입, 맵)
 * */
@RestController
@RequestMapping("/response")
public class ResponseRestController {

    // 문자열
    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    // 기본자료형 응답
    @GetMapping("/random")
    public int random() {
        return (int) (Math.random() * 10) + 1;
    }

    // Object 응답 JSON 객체 {}
    @GetMapping("/message")
    public Message getMessage() {
        return new Message(200, "메시지를 응답합니다.");
    }

    /*
     반환 타입별 JSON 변환
     * - List  -> 배열 [ ]   ex) ["사과", "바나나", "귤"]
    */
    // List 응답 JSON 배열 []
    @GetMapping("/list")
    public List<String> getList() {
        return List.of("사과", "바나나", "귤");
    }

    /*
     반환 타입별 JSON 변환
     * - Map, 일반 클래스 -> 객체 { }   ex) {"200": "정상응답", "404": "페이지를 찾을 수 없습니다"}
    */
    // Map 응답 JSON 객체 {}
    @GetMapping("/map")
    public Map<Integer, String> getMap() {
        List<Message> messageList = new ArrayList<>();
        messageList.add(new Message(200, "정상응답"));
        messageList.add(new Message(404, "페이지를 찾을 수 없습니다"));
        messageList.add(new Message(500, "개발자 잘못"));

        return messageList.stream().collect(Collectors.toMap(Message::getHttpStatusCode, Message::getMessage));
    }

    /* 정적 리소스(이미지파일) 응답
    * - produces 설정하지 않으면 text/html로 응답을 하기때문에 이미지가 전송되지 않음
    * => produces = MediaType.IMAGE_JPEG_VALUE 로 해줘야 이미지가 제대로 나온다
    * (byte[] -> 이미지 데이터 그대로 응답)
    * */
    @GetMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage() throws IOException {
        return getClass().getResourceAsStream("/static/cat1.jpg").readAllBytes();
    }

    /*
     ResponseEntity 응답 (상태코드 + 헤더 + 바디를 직접 설정)
     * - HTTP 요청에 대한 응답을 구성할 때 사용하는 클래스
     * - 응답 본문, 상태코드, 응답 헤더 등을 포함한 전체 HTTP 응답을 제어할 수 있다.
    * */
    @GetMapping("/entity")
    public ResponseEntity<Message> getEntity() {
        // ok(...) : 상태코드 200 + 바디
        return ResponseEntity.ok(new Message(200, "응답 성공"));

        // status(404) : 실제 HTTP 상태코드를 404로 설정 (클라이언트는 이 값으로 성공/실패 판단)
        // body(...)   : 응답 본문, Spring은 내용을 검사하지 않고 그대로 JSON으로 변환
        // => 상태코드와 바디의 메시지는 서로 별개이므로, 둘을 맞추는 건 개발자가 직접 해야 한다
        //    (404인데 "응답 성공"이 나오는 이유)
       //return ResponseEntity.status(404).body(new Message(404, "응답 성공"));
    }
}

