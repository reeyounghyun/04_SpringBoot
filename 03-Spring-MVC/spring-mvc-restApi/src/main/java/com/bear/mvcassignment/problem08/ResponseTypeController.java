package com.bear.mvcassignment.problem08;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 문제 8. @RestController 반환 타입별 응답
 *
 * 1. 이 클래스를 @RestController로 등록하고 클래스 공통 경로를 /api/responses로 설정한다.
 * 2. 아래 메서드의 GET 매핑을 작성하고 TODO를 완성한다.
 *
 * 이 Java 파일의 어노테이션과 TODO만 완성한다. 메서드 이름과 반환형은 유지한다.
 * 홈 화면의 문제 8 링크 또는 같은 폴더의 problem08.http로 응답을 확인한다.
 * 링크를 누르기 전에 응답이 어떻게 나올지 먼저 예상해본다.
 */
@RestController
@RequestMapping("/api/responses")
public class ResponseTypeController {

    /**
     * 문제 8-1. 문자열 응답
     *
     * 요청: GET /api/responses/text
     * 1. 문자열 "도서 API입니다."를 반환한다.
     *
     * 확인: 화면에 도서 API입니다. 가 그대로 표시된다. (View 이름으로 해석되지 않는다)
     *       Content-Type: text/plain
     */
    @GetMapping("/text")
    public String text() {
        //throw new UnsupportedOperationException("TODO 8-1");
        return "도서 API입니다.";
    }

    /**
     * 문제 8-2. 기본 자료형 응답
     *
     * 요청: GET /api/responses/count
     * 1. 보유 도서 수 3을 int로 반환한다.
     *
     * 확인: 3 / Content-Type: application/json
     */
    @GetMapping("/count")
    public int count() {
        //throw new UnsupportedOperationException("TODO 8-2");
        return BookCount.count();


    }

    /**
     * 문제 8-3. 객체 응답 -> JSON 객체 { }
     *
     * 요청: GET /api/responses/book
     * 1. new BookDTO(1, "자바의 정석", "남궁성", 30000)을 반환한다.
     *
     * 확인: {"no":1,"title":"자바의 정석","author":"남궁성","price":30000}
     *       (JSON key는 BookDTO의 getter 이름에서 만들어진다)
     */
    @GetMapping("/book")
    public BookDTO book(BookDTO bookes) {
        //throw new UnsupportedOperationException("TODO 8-3");
        BookDTO bookes = new BookDTO(1, "자바의정석", "남궁성", 30000);
        return bookes;
    }

    /**
     * 문제 8-4. List 응답 -> JSON 배열 [ ]
     *
     * 요청: GET /api/responses/books
     * 1. 아래 3권을 List.of(...)로 담아 반환한다.
     *    - 1, "자바의 정석", "남궁성", 30000
     *    - 2, "스프링 입문", "김영한", 25000
     *    - 3, "클린 코드", "로버트 C. 마틴", 33000
     *
     * 확인: [{"no":1,...},{"no":2,...},{"no":3,...}] 객체를 담은 배열
     */
    @GetMapping("/books")
    public List<BookDTO> books() {
        //throw new UnsupportedOperationException("TODO 8-4");
        BookDTO book1 = new BookDTO(1, "자바의정석", "남궁성" , 30000);
        BookDTO book2 = new BookDTO(2, "스프링입문", "김영환" , 25000);
        BookDTO book3 = new BookDTO(3, "클린 코드", "로버트 c" , 33000);
        return List.of(book1, book2, book3);
    }

    /**
     * 문제 8-5. Map 응답 -> JSON 객체 { }
     *
     * 요청: GET /api/responses/status
     * 1. LinkedHashMap에 아래 순서로 담아 반환한다.
     *    200 -> "정상응답", 404 -> "도서를 찾을 수 없습니다", 500 -> "서버 오류"
     *
     * 확인: {"200":"정상응답","404":"도서를 찾을 수 없습니다","500":"서버 오류"}
     *       Integer key가 JSON에서는 문자열("200")이 되는 것을 확인한다.
     */
    @GetMapping("/status")
    public Map<Integer, String> status() {
        //throw new UnsupportedOperationException("TODO 8-5");
        Map<Integer, String> map = new LinkedHashMap<>();
        map.put(200, "정상응답");
        map.put(404, "도서를 찾을 수 없습니다");
        map.put(500, "서버 오류");
        return map;
    }

    /**
     * 문제 8-6. 이미지 응답 (byte[])
     *
     * 요청: GET /api/responses/cover
     * 1. 매핑에 produces로 image/jpeg를 지정한다. (MediaType 상수 사용)
     * 2. 클래스패스의 /images/cover.jpg를 읽어 byte[]로 반환한다.
     *    getClass().getResourceAsStream(...).readAllBytes()
     *
     * 확인: 브라우저에 고양이 이미지가 표시된다.
     *       produces를 빼고 실행해보고 어떻게 달라지는지도 확인한다.
     */
    @GetMapping(value = "/cover", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] cover() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/images/cover.jpg")) {
            return is.readAllBytes();
        }
    }

    /**
     * 문제 8-7. ResponseEntity로 상태코드·헤더·바디 직접 설정
     *
     * 요청: GET /api/responses/entity
     * 1. 상태코드 202 Accepted (HttpStatus 상수 사용)
     * 2. 응답 헤더 X-Book-Count: 3
     * 3. 바디는 8-3과 같은 BookDTO
     * ResponseEntity.status(...).header(...).body(...) 메서드 체이닝으로 작성한다.
     *
     * 확인: problem08.http로 요청하면 202, X-Book-Count: 3 헤더, 도서 JSON이 보인다.
     */
    @GetMapping("/entity")
    public ResponseEntity<BookDTO> entity() {
        //throw new UnsupportedOperationException("TODO 8-7");
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .header("X-Book-Count", "3")
                .body(new BookDTO(1L, "스프링 부트", "김영한"));
    }

}
