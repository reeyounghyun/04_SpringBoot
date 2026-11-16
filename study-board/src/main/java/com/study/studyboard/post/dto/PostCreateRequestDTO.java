package com.study.studyboard.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Size
@NotBlank
@NotNull
public class PostCreateRequestDTO {
    // 자료등록
    private String title;
    private String body;
    private Long categoryId;
    private Long authorId;
    private String referenceUrl;
}
