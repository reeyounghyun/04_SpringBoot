package com.bear.studyboard.comment.dto;

import com.bear.studyboard.comment.entity.Comment;
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
public class CommentResponseDTO {

    private long id;
    private long postId;
    private long authorId;
    private String authorName;
    private String body;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CommentResponseDTO from(Comment comment, long postId) {
        return new CommentResponseDTO(
                comment.getId(),
                postId,
                comment.getAuthor().getId(),
                comment.getAuthor().getNickname(),
                comment.getBody(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
