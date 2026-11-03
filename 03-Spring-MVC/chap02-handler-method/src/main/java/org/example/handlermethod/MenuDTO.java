package org.example.handlermethod;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MenuDTO {

    // 필드 이름은 폼 input의 name과 같아야 @ModelAttribute로 값이 바인딩된다.
    private String name;
    private int price;
    private int categoryCode;
    private String orderableStatus;
}
