package com.bear.studyboard.post.controller;

import com.bear.studyboard.common.dto.ApiResponse;
import com.bear.studyboard.post.dto.PostSummaryResponseDTO;
import com.bear.studyboard.post.service.PostSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostSummaryController {
    // 게시글 AI 요약 Controlle

    // 의존성 주입
    //private final PostSummaryController postSummaryController;
    private final PostSummaryService postSummaryService;


    /*
     * GET이 아니라 POST를 쓰는이유
     * - GET은 몇번 호출해도 결과가 같고, 서버에 부담을 주지않는 조회에서 사용
     * - 요약은 호출할때마다 비용이 발생하고 결과가 달라질수있음 -> POST 사용근거
     * */

    @PostMapping("/{postId}/summary")
    public ResponseEntity<ApiResponse<PostSummaryResponseDTO>> summary(@PathVariable long postId) {

        PostSummaryResponseDTO summary = postSummaryService.summarize(postId);

        ApiResponse<PostSummaryResponseDTO> response = ApiResponse.of(HttpStatus.OK, "게시글 요약완료", summary);

        return ResponseEntity.ok(response);
    }
}
