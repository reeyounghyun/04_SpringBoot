package com.bear.studyboard.comment.dto;

import com.bear.studyboard.comment.entity.Comment;
import com.bear.studyboard.member.dto.AuthorResponseDTO;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentResponseDTO {

    private long id;
    private long postId;
    private String body;
    private AuthorResponseDTO author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CommentResponseDTO from(Comment comment, long postId) {
        AuthorResponseDTO author = AuthorResponseDTO.from(comment.getAuthor());
        LocalDateTime createdAt = comment.getCreatedAt();
        LocalDateTime updatedAt = comment.getUpdatedAt();
        return new CommentResponseDTO(comment.getId(), postId, comment.getBody(), author, createdAt, updatedAt);
    }
}
