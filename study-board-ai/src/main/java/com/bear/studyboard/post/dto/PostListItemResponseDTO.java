package com.bear.studyboard.post.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostListItemResponseDTO {

    private String authorName;
    private String title;
    private LocalDateTime createdAt;

}
