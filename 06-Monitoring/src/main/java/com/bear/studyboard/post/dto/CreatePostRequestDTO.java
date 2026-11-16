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

    private long authorId;
    private long categoryId;
    private String title;
    private String body;
    private String referenceUrl;
}
