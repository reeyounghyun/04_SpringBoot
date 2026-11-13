package com.bear.studyboard.comment.controller;

import com.bear.studyboard.comment.dto.CommentResponseDTO;
import com.bear.studyboard.comment.dto.CreateCommentRequestDTO;
import com.bear.studyboard.comment.service.CommentService;
import com.bear.studyboard.common.dto.ApiResponse;
import com.bear.studyboard.common.dto.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<CommentResponseDTO>> createComment(
            @PathVariable long postId,
            @RequestBody CreateCommentRequestDTO request
    ) {

        CommentResponseDTO comment = commentService.createComment(postId, request);

        HttpStatus status = HttpStatus.CREATED;
        ApiResponse<CommentResponseDTO> response = ApiResponse.of(status, "댓글을 등록했습니다.", comment);

        return ResponseEntity.status(status).body(response);
    }

    // 페이징 요청 방식은 게시글 목록과 같다. → StudyPostController.findPosts() 참고
    // GET /api/posts/1/comments?page=0&size=20
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<PageResponseDTO<CommentResponseDTO>>> findComments(
            @PathVariable long postId,
            @PageableDefault(size = 20) Pageable pageable
    ) {

        PageResponseDTO<CommentResponseDTO> commentPage = commentService.findComments(postId, pageable);

        ApiResponse<PageResponseDTO<CommentResponseDTO>> response =
                ApiResponse.of(HttpStatus.OK, "댓글 목록을 조회했습니다.", commentPage);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponseDTO>> updateComment(
            @PathVariable long commentId,
            @RequestBody String body
    ) {

        CommentResponseDTO comment = commentService.updateComment(commentId, body);

        ApiResponse<CommentResponseDTO> response = ApiResponse.of(HttpStatus.OK, "댓글을 수정했습니다.", comment);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable long commentId) {

        commentService.deleteComment(commentId);

        return ResponseEntity.noContent().build();
    }
}
