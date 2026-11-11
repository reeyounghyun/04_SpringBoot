package com.study.studyboard.post.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
// 게시판 수정이라, final은 사용하지않는다.
public class PostUpdateRequestDTO {
    private String title;
    private String body;
    private Long categoryId;
    private String referenceUrl;
}
