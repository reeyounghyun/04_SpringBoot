package org.example.jpaintro.dto;

import lombok.*;

/**
 * [DTO] 클라이언트 → 서버 로 "들어오는" 데이터
 *
 * - 메뉴 등록/수정 시 요청 본문(JSON)을 담는 객체이다.
 *   예) { "menuName": "김치찌개", "menuPrice": 8000, "categoryCode": 4 }
 * - PK(menuCode)는 DB 가 자동 생성하므로 보통 포함하지 않는다.
 * - Entity 를 직접 받지 않고 DTO 로 받는 이유 :
 *   클라이언트가 바꾸면 안 되는 값(PK 등)을 막고, DB 구조를 노출하지 않기 위해서.
 *
 * 흐름: [JSON] → MenuRequestDTO → (Service 에서 변환) → Menu(Entity) → DB
 */
@Getter             // 모든 필드의 getter 생성
@Setter             // 모든 필드의 setter 생성
@ToString           // toString() 생성 (객체 내용 출력용)
@AllArgsConstructor // 모든 필드를 받는 생성자 생성
@NoArgsConstructor  // 기본 생성자 생성 (JSON → 객체 변환 시 필요)

// 요청 전용 DTO
public class MenuRequestDTO {

    private String menuName;
    private Integer menuPrice;
    private Integer categoryCode;
    private String orderableStatus;
}
