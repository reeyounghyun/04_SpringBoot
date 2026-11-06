package org.example.jpaintro.dto;

import org.example.jpaintro.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * [DTO] 서버 → 클라이언트 로 "나가는" 데이터
 *
 * - 조회/등록 결과를 응답(JSON)으로 돌려줄 때 사용하는 객체이다.
 *   예) { "menuCode": 25, "menuName": "김치찌개", "menuPrice": 8000, "categoryCode": 4 }
 * - 저장 후 생성된 PK(menuCode)를 알려주기 위해 포함한다.
 * - 보여주면 안 되는 값(비밀번호 등)은 넣지 않는다.
 *
 * 흐름: DB → Menu(Entity) → (Service 에서 변환) → MenuResponseDTO → [JSON]
 */
// 응답전용 DTO
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuResponseDTO {

    private Integer menuCode;
    private String menuName;
    private Integer menuPrice;
    private Integer categoryCode;
    private String categoryName;
    private String orderableStatus;

    // Entity(Menu) → 응답 DTO 로 변환하는 정적 팩토리 메서드
    // Menu 클래스를 MenuResponseDTO로 변환시키는 팩토리 메서드
    public static MenuResponseDTO from(Menu menu) {
        return new MenuResponseDTO(
                menu.getMenuCode(),
                menu.getMenuName(),
                menu.getMenuPrice(),
                menu.getCategory().getCategoryCode(),
                menu.getCategory().getCategoryName(),
                menu.getOrderableStatus()
        );
    }
}
