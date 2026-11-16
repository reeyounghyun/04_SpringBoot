package org.example.springdatajpa.dto;

import lombok.*;

// 요청 전용 DTO
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MenuRequestDTO {

    private String menuName;
    private Integer menuPrice;
    private Integer categoryCode;
    private String orderableStatus;
}
