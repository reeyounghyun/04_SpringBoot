package com.study.studyboard.post.dto;

import com.study.studyboard.post.entity.StudyPost;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostDetailResponseDTO {
    // 사용자한테 보여줄 필드
    private Long postId;    //아이디
    private String title;   //제목
    private String content; //본문
    private String categoryName;  //카테고리(분류)
    private String authorName; //작성자
    private String link; //선택 필드로 null일 수 있다.

    public static PostDetailResponseDTO from(StudyPost studyPost) {

        return new PostDetailResponseDTO(
                studyPost.getId(),
                studyPost.getTitle(),
                studyPost.getBody(),
                studyPost.getCategory().getName(),
                studyPost.getAuthor().getNickname(), // 찾아오는 로직을 사용함
                studyPost.getReferenceUrl()
        );
    }
}

