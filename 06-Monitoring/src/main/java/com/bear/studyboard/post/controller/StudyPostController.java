package com.bear.studyboard.post.controller;

import com.bear.studyboard.common.dto.ApiResponse;
import com.bear.studyboard.common.dto.PageResponseDTO;
import com.bear.studyboard.post.dto.CreatePostRequestDTO;
import com.bear.studyboard.post.dto.PostDetailResponseDTO;
import com.bear.studyboard.post.dto.PostListItemResponseDTO;
import com.bear.studyboard.post.dto.UpdatePostRequestDTO;
import com.bear.studyboard.post.service.StudyPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class StudyPostController {

    private final StudyPostService studyPostService;

    @PostMapping
    public ResponseEntity<ApiResponse<PostDetailResponseDTO>> createPost(@RequestBody CreatePostRequestDTO request) {

        PostDetailResponseDTO post = studyPostService.createPost(request);

        HttpStatus status = HttpStatus.CREATED;
        ApiResponse<PostDetailResponseDTO> response = ApiResponse.of(status, "게시글을 등록했습니다.", post);

        return ResponseEntity.status(status).body(response);
    }

    // page는 0부터 시작한다. 페이지 크기의 기본값은 20이다.
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponseDTO<PostListItemResponseDTO>>> findPosts(
            @PageableDefault(size = 20) Pageable pageable
    ) {

        PageResponseDTO<PostListItemResponseDTO> postPage = studyPostService.findPosts(pageable);

        ApiResponse<PageResponseDTO<PostListItemResponseDTO>> response =
                ApiResponse.of(HttpStatus.OK, "게시글 목록을 조회했습니다.", postPage);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostDetailResponseDTO>> findPost(@PathVariable long postId) {

        PostDetailResponseDTO post = studyPostService.findPost(postId);

        ApiResponse<PostDetailResponseDTO> response = ApiResponse.of(HttpStatus.OK, "게시글을 조회했습니다.", post);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostDetailResponseDTO>> updatePost(
            @PathVariable long postId,
            @RequestBody UpdatePostRequestDTO request
    ) {

        PostDetailResponseDTO post = studyPostService.updatePost(postId, request);

        ApiResponse<PostDetailResponseDTO> response = ApiResponse.of(HttpStatus.OK, "게시글을 수정했습니다.", post);

        return ResponseEntity.ok(response);
    }
}
