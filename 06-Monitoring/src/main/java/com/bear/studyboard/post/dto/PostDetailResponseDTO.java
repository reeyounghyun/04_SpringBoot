package com.bear.studyboard.post.dto;

import com.bear.studyboard.post.entity.StudyPost;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostDetailResponseDTO {

    private long id;
    private long authorId;
    private String authorName;
    private long categoryId;
    private String categoryName;
    private String title;
    private String body;
    private String referenceUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PostDetailResponseDTO from(StudyPost post) {
        return new PostDetailResponseDTO(
                post.getId(),
                post.getAuthor().getId(),
                post.getAuthor().getNickname(),
                post.getCategory().getId(),
                post.getCategory().getName(),
                post.getTitle(),
                post.getBody(),
                post.getReferenceUrl(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
