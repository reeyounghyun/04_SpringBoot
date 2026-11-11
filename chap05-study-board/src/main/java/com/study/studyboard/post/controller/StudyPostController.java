package com.study.studyboard.post.controller;


import com.study.studyboard.post.dto.PostCreateRequestDTO;
import com.study.studyboard.common.dto.ApiResponse;
import com.study.studyboard.common.dto.PageResponseDTO;
import com.study.studyboard.post.dto.PostDetailResponseDTO;
import com.study.studyboard.post.dto.PostListItemResponseDTO;
import com.study.studyboard.post.dto.PostUpdateRequestDTO;
import com.study.studyboard.post.service.StudyPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
// 게시글 기능만 모아둔 패키지
public class StudyPostController {

    private final StudyPostService studyPostService;

    /**
     * 페이징(Paging)
     * - 데이터를 전부주지 않고 몇번째 페이지를 한번에 몇 개씩 나누어 주는 방식
     *
     * Pageable -> 페이지번호, 페이지크기, 정렬 정보
     * - Spring Data가 제공하는 "페이지 요청 정보" 인터페이스
     * - /api/posts?page=1&size=10&sort=createdAt,desc
     * page -> 페이지 번호(0부터)
     * size -> 한번에 담을 갯수
     * sort -> 정렬기준
     * */
    // 게시글 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponseDTO<PostListItemResponseDTO>>> findPosts(
            @PageableDefault(size = 20) Pageable pageable
    ) {
        PageResponseDTO<PostListItemResponseDTO> postPage = studyPostService.findPosts(pageable);
        return ResponseEntity
                .ok(ApiResponse.of(HttpStatus.OK, "게시물 목록조회 완료", postPage));
    }

    // 게시글 상세
    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponseDTO> findPost(@PathVariable long postId) throws Throwable {
        PostDetailResponseDTO post = studyPostService.detailFindPost(postId);
        return ResponseEntity.ok(post);
    }

    // 자료 등록
    @PostMapping
    public ResponseEntity<ApiResponse<PostDetailResponseDTO>> createPost(@Valid @RequestBody PostCreateRequestDTO request) {

        PostDetailResponseDTO createPost = studyPostService.createPost(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED, "게시물 등록 완료", createPost));
    }

    // 자료 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostDetailResponseDTO>> updatePost (
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateRequestDTO request
    ) {
        PostDetailResponseDTO updated =  studyPostService.updatePost(postId, request);

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK, "게시물 수정 완료", updated));
    }
}
