package com.study.studyboard.common.dto;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

// Spring Data의 Page<T>에서 필요한 페이징 정보(목록, 현재 페이지, 크기, 전체 개수)만 골라
// 클라이언트에 내려주기 위해 우리가 직접 만든 공통 페이징 응답 객체
public class PageResponseDTO<T> {

    private List<T> items;
    private int page;
    private int size;
    private long totalElements;


    // Page -> Pageable, sort 정보들도 포함하고 있는데,
    // 그래서 이 내용이 출력되지 않도록 클라이언트에게 필요한 내용만 PageResponseDTO로 분리해서 사용한다.
    public static <T> PageResponseDTO<T> from(Page<T> page) {
        return new PageResponseDTO<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements());
    }
}
