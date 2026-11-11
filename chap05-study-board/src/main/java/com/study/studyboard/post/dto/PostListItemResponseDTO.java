package com.study.studyboard.post.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostListItemResponseDTO {
    private String authorName; // 작성자
    private String title; // 제목
    private LocalDateTime createdAt; // 작성일

}
