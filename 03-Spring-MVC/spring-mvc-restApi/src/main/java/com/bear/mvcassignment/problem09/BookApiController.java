package com.bear.mvcassignment.problem09;

import com.bear.mvcassignment.problem08.BookDTO;
// [수정 전] import org.apache.logging.log4j.message.Message;
// [수정 후] 삭제 - 사용하지 않는 import (자동완성으로 잘못 들어온 것)
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// [수정 전] import static aQute.bnd.annotation.headers.Category.users;
// [수정 후] 삭제 - users라는 변수는 이 문제와 관계없음. 자동완성이 엉뚱한 라이브러리의 users를 import한 것

/**
 * 문제 9. ResponseEntity로 도서 CRUD API 만들기
 *
 * 1. 이 클래스를 @RestController로 등록하고 클래스 공통 경로를 /api/books로 설정한다.
 * 2. 아래 메서드의 요청 매핑과 매개변수 바인딩 어노테이션(@PathVariable, @RequestBody)을 작성한다.
 * 3. 모든 응답은 ResponseEntity로 상태코드를 직접 지정한다.
 *    없는 번호로 요청하면 500(예외)이 아니라 404가 나와야 한다.
 *    번호로 도서를 찾을 때는 for문으로 찾고, 못 찾으면(null) 404를 반환한다.
 *
 * 이 Java 파일의 어노테이션과 TODO만 완성한다. 메서드 이름과 반환형은 유지한다.
 * books 필드와 생성자는 제공 코드이므로 수정하지 않는다. (서버를 재시작하면 처음 3권으로 돌아간다)
 * 같은 폴더의 problem09.http로 POST/PUT/DELETE 요청을 보내 확인한다.
 *
 * 응답 바디 형식 (ResponseMessage)
 * - 성공: new ResponseMessage(200, "조회성공", Map.of("book", 도서))
 * - 실패: new ResponseMessage(404, "도서를 찾을 수 없습니다.", null)
 */
@RestController
@RequestMapping("/api/books")
public class BookApiController {

    private final List<BookDTO> books = new ArrayList<>();

    public BookApiController() {
        books.add(new BookDTO(1, "자바의 정석", "남궁성", 30000));
        books.add(new BookDTO(2, "스프링 입문", "김영한", 25000));
        books.add(new BookDTO(3, "클린 코드", "로버트 C. 마틴", 33000));
    }

    /**
     * 문제 9-1. 전체 조회
     *
     * 요청: GET /api/books
     * 1. 200 OK + ResponseMessage(200, "조회성공", {"books": books})
     *
     * 확인: {"httpStatus":200,"message":"조회성공","result":{"books":[...3권...]}}
     */
    // [수정 전] @GetMapping("/api/books")
    // [수정 후] @GetMapping
    //   이유: 클래스에 이미 @RequestMapping("/api/books")가 있어서
    //        메서드에 또 쓰면 실제 주소가 /api/books/api/books 가 된다.
    @GetMapping
    public ResponseEntity<ResponseMessage> findAllBooks() {
       // throw new UnsupportedOperationException("TODO 9-1");

        Map<String, Object> responseMap = new HashMap<>();
        // [수정 전] responseMap.put("users", users);
        // [수정 후] 키는 "books", 값은 이 클래스의 books 리스트
        responseMap.put("books", books);

        ResponseMessage result = new ResponseMessage(200, "조회성공", responseMap);

        return ResponseEntity.ok(result);
    }

    /**
     * 문제 9-2. 단건 조회
     *
     * 요청: GET /api/books/{no}
     * 1. @PathVariable로 no를 받는다.
     * 2. 있으면 200 OK + ResponseMessage(200, "조회성공", {"book": 도서})
     * 3. 없으면 404 + ResponseMessage(404, "도서를 찾을 수 없습니다.", null)
     *
     * 확인: /api/books/1 -> 200 자바의 정석
     *       /api/books/99 -> 404 (빈 객체 + 200이 나오면 틀린 것)
     */
    @GetMapping("/{no}")
    // [수정 전] public ResponseEntity<ResponseMessage> findBook(int no) {
    // [수정 후] @PathVariable 추가 - 없으면 URL의 {no} 값이 no에 들어오지 않는다.
    public ResponseEntity<ResponseMessage> findBook(@PathVariable int no) {
        //throw new UnsupportedOperationException("TODO 9-2");

        // [수정 전]
        //   Map<String, Object> responseMap = new HashMap<>();
        //   responseMap.put("book", book);          // book은 아래 for문 안에서만 존재 → 컴파일 오류
        //   if(books.get(no) != null) { ... }       // get(no)는 "인덱스" → 1번 책이 아닌 2번째 책, 99는 예외(500)
        //   ResponseMessage result;                  // if/else 안의 result와 다른 변수, 값 없음 → 컴파일 오류
        //   return ResponseEntity.ok().body(result); // 못 찾아도 200이 나감
        // [수정 후] for문으로 번호를 비교해 찾고, 없으면 그 자리에서 바로 404 return

        BookDTO foundBook = null;
        for (BookDTO book : books) {
            if (book.getNo() == no) {
                foundBook = book;
                break;
            }
        }
        if (foundBook == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage(404, "도서를 찾을 수 없습니다.", null));
        }

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("book", foundBook);
        return ResponseEntity.ok(new ResponseMessage(200, "조회성공", responseMap));
    }

    /**
     * 문제 9-3. 등록 + 유효성 검사
     *
     * 요청: POST /api/books  (본문: {"title":"...","author":"...","price":36000})
     * 1. @RequestBody로 BookDTO를 받는다.
     * 2. price가 null이거나 0 이하면 400 Bad Request
     *    + ResponseMessage(400, "가격은 0보다 커야 합니다.", null)
     * 3. 새 번호 = 마지막 도서 번호 + 1 (books가 비어 있으면 1) 로 설정하고 books에 추가한다.
     * 4. 201 Created + Location 헤더 /api/books/{새번호}, 바디 없음
     *    ResponseEntity.created(URI) 사용
     *
     * 확인: 이펙티브 자바 등록 -> 201, Location: /api/books/4
     *       이어서 GET /api/books/4 로 조회된다.
     *       price 0으로 등록 -> 400
     */
    @PostMapping
    public ResponseEntity<ResponseMessage> registBook(@RequestBody BookDTO newBook) {
        //throw new UnsupportedOperationException("TODO 9-3");
        if(newBook.getPrice() == null || newBook.getPrice() <= 0){
            ResponseMessage result = new ResponseMessage(400, "가격은 0보다 커야 합니다.", null);
            // [수정 전] return ResponseEntity.ok().body(result);
            // [수정 후] ok()는 200이다. 유효성 검사 실패는 400 Bad Request
            return ResponseEntity.badRequest().body(result);
        }

        // [수정 전] int newNo = books.size() + 1;
        // [수정 후] "마지막 도서 번호 + 1", 비어 있으면 1
        //   이유: size()+1은 중간 책이 삭제되면 번호가 겹친다.
        //        예) 1,2,3 중 2 삭제 → size=2 → 새 번호 3 → 기존 3번과 중복
        int newNo = books.isEmpty() ? 1 : books.get(books.size() - 1).getNo() + 1;
        newBook.setNo(newNo);

        // [수정 전] books.post(newNo, newBook);
        // [수정 후] List에는 post() 메서드가 없다. 추가는 add()
        books.add(newBook);

        // [수정 전] return ResponseEntity.created(URI.create("/api/books/" + newNo)).location(URI.create("/api/books/" + newNo)).build();
        // [수정 후] created(URI)가 이미 Location 헤더를 넣어 주므로 .location(...)은 중복
        return ResponseEntity.created(URI.create("/api/books/" + newNo)).build();
    }

    /**
     * 문제 9-4. 전체 수정
     *
     * 요청: PUT /api/books/{no}  (본문: title, author, price 모두 전달)
     * 1. URL의 no로 수정할 도서를 찾는다. (본문의 값으로 찾지 않는다 - URL이 리소스를 식별한다)
     * 2. 없으면 404 + ResponseMessage(404, "도서를 찾을 수 없습니다.", null)
     * 3. 있으면 title, author, price를 본문의 값으로 모두 바꾼다.
     * 4. 200 OK + ResponseMessage(200, "수정성공", {"book": 수정된 도서})
     *
     * 확인: PUT /api/books/1 -> 200, 이후 GET /api/books/1 에 바뀐 값이 보인다.
     *       PUT /api/books/99 -> 404
     */
    @PutMapping("/{no}")
    // [수정 전] public ResponseEntity<ResponseMessage> modifyBook(int no, BookDTO modifyBook) {
    // [수정 후] @PathVariable(URL의 번호) + @RequestBody(JSON 본문) 추가
    public ResponseEntity<ResponseMessage> modifyBook(@PathVariable int no,
                                                      @RequestBody BookDTO modifyBook) {
        //throw new UnsupportedOperationException("TODO 9-4");

        // [수정 전] 메서드 본문 비어 있음 (return 없음 → 컴파일 오류)
        // [수정 후] 1) URL의 no로 찾기
        BookDTO foundBook = null;
        for (BookDTO book : books) {
            if (book.getNo() == no) {
                foundBook = book;
                break;
            }
        }

        // 2) 없으면 404
        if (foundBook == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage(404, "도서를 찾을 수 없습니다.", null));
        }

        // 3) 본문 값으로 전부 교체 - foundBook은 리스트 안의 그 객체라서 리스트에도 바로 반영된다
        foundBook.setTitle(modifyBook.getTitle());
        foundBook.setAuthor(modifyBook.getAuthor());
        foundBook.setPrice(modifyBook.getPrice());

        // 4) 200 OK + 수정된 책
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("book", foundBook);
        return ResponseEntity.ok(new ResponseMessage(200, "수정성공", responseMap));
    }

    /**
     * 문제 9-6. 삭제
     *
     * 요청: DELETE /api/books/{no}
     * 1. 없으면 404 + ResponseMessage(404, "도서를 찾을 수 없습니다.", null)
     * 2. 있으면 books에서 제거하고 204 No Content, 바디 없음
     *
     * 확인: DELETE /api/books/2 -> 204, 다시 보내면 404
     */
    // [수정 전] @DeleteMapping("/api/books/{no}")
    // [수정 후] @DeleteMapping("/{no}") - 클래스 경로와 중복 제거
    @DeleteMapping("/{no}")

    // [수정 전] public ResponseEntity<ResponseMessage> removeBook(int no) {
    // [수정 후] @PathVariable 추가
    public ResponseEntity<ResponseMessage> removeBook(@PathVariable int no) {
        //throw new UnsupportedOperationException("TODO 9-6");

        // [수정 전] 메서드 본문 비어 있음 (return 없음 → 컴파일 오류)
        // [수정 후] for문으로 찾기 → 없으면 404 → 있으면 remove 후 204
        BookDTO foundBook = null;
        for (BookDTO book : books) {
            if (book.getNo() == no) {
                foundBook = book;
                break;
            }
        }

        if (foundBook == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage(404, "도서를 찾을 수 없습니다.", null));
        }

        // for문 안에서 remove하면 ConcurrentModificationException이 날 수 있어서 찾은 뒤 밖에서 삭제
        books.remove(foundBook);

        // 204 No Content는 바디가 없으므로 build()
        return ResponseEntity.noContent().build();
    }
}
