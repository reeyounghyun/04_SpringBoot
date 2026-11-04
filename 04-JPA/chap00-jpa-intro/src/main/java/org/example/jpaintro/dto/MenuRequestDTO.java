package org.example.jpaintro.dto;

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
public class MenuRequestDTO {
}
