package com.study.springai.section04.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
// Spring AI 객체를 생성하고 값을 채울 수 있도록 기본 생성자 Setter 필수!
public class PostAnalysis {

    // 게시글 분석 결과를 담당하는 DTO

    private String summary;
    private String category;
    private List<String> tags;

}
