package org.example.restapi.testpractice.testsection03.valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/practice/valid")
public class ProductValidController {

    /*
     * [문제 2] 요청 JSON이 ProductDTO 규칙대로 검사되도록 매개변수에 어노테이션을 추가하세요.
     * */
    @PostMapping("/products")
    public ResponseEntity<Void> saveProduct(@RequestBody ProductDTO productDTO) {

        System.out.println("productDTO = " + productDTO);

        return ResponseEntity.ok().build();
    }
}
