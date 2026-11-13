package com.bear.studyboard.post.dto;

import com.bear.studyboard.category.dto.CategoryResponseDTO;
import com.bear.studyboard.member.dto.AuthorResponseDTO;
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
    private String title;
    private String body;
    private AuthorResponseDTO author; //저자
    private CategoryResponseDTO category;
    private String referenceUrl; //참고 자료 링크
    private LocalDateTime createdAt; //처음 작성한 시각(한 번 정해지면 안 바뀜)
    private LocalDateTime updatedAt; //마지막으로 수정한 시각(글을 수정할 때마다 갱신)

    private PostDetailResponseDTO(StudyPost post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.body = post.getBody();
        this.author = AuthorResponseDTO.from(post.getAuthor());
        this.category = CategoryResponseDTO.from(post.getCategory());
        this.referenceUrl = post.getReferenceUrl();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }

    public static PostDetailResponseDTO from(StudyPost post) {
        return new PostDetailResponseDTO(post);
    }
}
