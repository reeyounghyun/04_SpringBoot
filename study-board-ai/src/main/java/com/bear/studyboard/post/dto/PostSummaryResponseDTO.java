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
public class PostSummaryResponseDTO {
// 게시글 AI 요약 DTO

    private long postId; // Client → id를 붙여준 이유는 정체를 알기 쉽게하기위함이다.
    private String title;
    private String summary; // ai가 작성한 요약
}
