package org.example.restapi.testpractice.testsection03.exceptionenum;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/practice/enum-errors")
public class ProductExceptionController {

    // 상품번호 → 상품명
    private final Map<Integer, String> products = Map.of(
            1, "키보드",
            2, "마우스",
            3, "모니터"
    );

    // 상품번호 → 재고 수량
    private final Map<Integer, Integer> stocks = Map.of(
            1, 10,
            2, 0,   // 마우스는 품절
            3, 5
    );

    // 상품 단건 조회
    @GetMapping("/products/{productNo}")
    public ResponseEntity<Map<String, Object>> findProduct(@PathVariable int productNo) {

        // [문제 7-1] productNo가 1보다 작으면 → INVALID_PRODUCT_NO 예외

        String productName = products.get(productNo);

        // [문제 7-2] productName이 null이면 → PRODUCT_NOT_FOUND 예외
        //           상세 메시지: "상품번호 {productNo}에 해당하는 상품이 없습니다."

        return ResponseEntity.ok(Map.of(
                "productNo", productNo,
                "name", productName
        ));
    }

    // 상품 주문
    @PostMapping("/products/{productNo}/order")
    public ResponseEntity<Map<String, Object>> order(@PathVariable int productNo,
                                                     @RequestParam int quantity) {

        // [문제 7-3] 없는 상품이면 → PRODUCT_NOT_FOUND 예외
        //           (힌트: Map의 어떤 메서드로 "이 키가 있나?"를 확인할 수 있을까요?)

        // [문제 7-4] quantity가 1보다 작으면 → INVALID_QUANTITY 예외

        int stock = stocks.getOrDefault(productNo, 0);

        // [문제 7-5] quantity가 재고(stock)보다 많으면 → OUT_OF_STOCK 예외
        //           상세 메시지: "현재 재고는 {stock}개입니다."

        return ResponseEntity.ok(Map.of(
                "productNo", productNo,
                "quantity", quantity,
                "remainStock", stock - quantity
        ));
    }
}
