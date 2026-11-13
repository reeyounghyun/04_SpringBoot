package com.bear.studyboard.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PageResponseDTO<T> {

    private List<T> items;
    private int page;
    private int size;
    private long totalElements;

    // 목록과 페이지 정보를 API 응답 DTO로 옮긴다.
    public static <T> PageResponseDTO<T> from(Page<T> page) {
        List<T> items = page.getContent();
        int pageNumber = page.getNumber();
        int pageSize = page.getSize();
        long totalElements = page.getTotalElements();
        return new PageResponseDTO<>(items, pageNumber, pageSize, totalElements);
    }
}
