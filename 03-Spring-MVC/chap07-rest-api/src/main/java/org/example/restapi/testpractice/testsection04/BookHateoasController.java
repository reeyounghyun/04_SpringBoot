package org.example.restapi.testpractice.testsection04;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/practice/hateoas")
public class BookHateoasController {

    private final List<BookDTO> books = new ArrayList<>();
    private final BookModelAssembler assembler;

    // 생성자 주입: Spring이 만들어 둔 BookModelAssembler를 넣어줘요
    public BookHateoasController(BookModelAssembler assembler) {
        this.assembler = assembler;

        books.add(new BookDTO(1, "자바의 정석", "남궁성", 30000));
        books.add(new BookDTO(2, "스프링 입문", "김영한", 25000));
        books.add(new BookDTO(3, "JPA 프로그래밍", "김영한", 38000));
    }

    // 도서 전체 조회
    @GetMapping("/books")
    public ResponseEntity<BookResponseMessage> findAllBooks() {

        /*
         * [문제 2] books를 그대로 담지 말고,
         *         stream() + assembler로 List<EntityModel<BookDTO>>로 바꿔서 담으세요.
         * */

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("books", books);

        return ResponseEntity.ok(new BookResponseMessage(200, "조회 성공", responseMap));
    }

    // 도서 단건 조회
    @GetMapping("/books/{bookNo}")
    public ResponseEntity<BookResponseMessage> findBookByNo(@PathVariable int bookNo) {

        BookDTO foundBook = null;

        for (BookDTO book : books) {
            if (book.getNo() == bookNo) {
                foundBook = book;
                break;
            }
        }

        // [문제 3] foundBook이 null이면 404 Not Found 응답

        // [문제 4] foundBook을 assembler로 감싸서 담으세요.
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("book", foundBook);

        return ResponseEntity.ok(new BookResponseMessage(200, "조회 성공", responseMap));
    }

    /*
     * [보너스 2] 저자별 도서 조회
     *  - GET /practice/hateoas/books/search?author=김영한
     *  - 저자가 같은 도서만 골라서(filter) 링크를 붙여 응답
     *  - 주의: "/books/search"와 "/books/{bookNo}" 중 Spring은 더 구체적인 "/books/search"를 먼저 매칭해요.
     * */
}
