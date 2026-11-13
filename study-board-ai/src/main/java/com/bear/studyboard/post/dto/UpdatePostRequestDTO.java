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
public class UpdatePostRequestDTO {

    private String title;
    private String body;
    private Long categoryId;
    private String referenceUrl;
}
