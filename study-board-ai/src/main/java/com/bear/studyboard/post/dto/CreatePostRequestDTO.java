package com.bear.studyboard.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreatePostRequestDTO {

    private String title;
    private String body;
    private Long categoryId;
    private Long authorId;
    private String referenceUrl;
}
