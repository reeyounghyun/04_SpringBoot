package com.bear.studyboard.comment.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateCommentRequestDTO {

    private Long authorId;

    private String body;
}
