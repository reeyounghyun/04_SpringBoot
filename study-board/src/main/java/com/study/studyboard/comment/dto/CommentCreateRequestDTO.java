package com.study.studyboard.comment.dto;

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
public class CommentCreateRequestDTO {

    private long authorId;
    private String body;

}
