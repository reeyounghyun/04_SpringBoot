package org.example.restapi.testpractice.testsection04;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BookDTO {

    private int no;         // 도서 번호
    private String title;   // 제목
    private String author;  // 저자
    private int price;      // 가격
}
