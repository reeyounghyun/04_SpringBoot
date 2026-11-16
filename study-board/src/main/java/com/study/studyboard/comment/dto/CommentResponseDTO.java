package com.study.studyboard.comment.dto;

import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor(access = PROTECTED)

public class CommentResponseDTO {
    private long authorId;
    private String name;

}
