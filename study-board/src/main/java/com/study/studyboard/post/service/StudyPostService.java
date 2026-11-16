package com.study.studyboard.post.service;

import com.study.studyboard.post.dto.PostCreateRequestDTO;
import com.study.studyboard.category.entity.Category;
import com.study.studyboard.category.repository.CategoryRepository;
import com.study.studyboard.common.dto.PageResponseDTO;
import com.study.studyboard.common.error.BusinessException;
import com.study.studyboard.common.error.ErrorCode;
import com.study.studyboard.member.entity.Member;
import com.study.studyboard.member.repository.MemberRepository;
import com.study.studyboard.post.dto.PostDetailResponseDTO;
import com.study.studyboard.post.dto.PostListItemResponseDTO;
import com.study.studyboard.post.dto.PostUpdateRequestDTO;
import com.study.studyboard.post.entity.StudyPost;
import com.study.studyboard.post.repository.StudyPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyPostService {

    private final StudyPostRepository studyPostRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;

    public PageResponseDTO<PostListItemResponseDTO> findPosts(Pageable pageable) {
        /*
         * PageRequest : Pageable 인터페이스의 대표 구현체
         * PageRequest.of(페이지 번호, 페이지 크기)
         * PageRequest.of(페이지 번호, 페이지 크기, 정렬)
         * */
        Pageable pageRequest = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt", "id")
        );

        // Page로 래핑된 DTO Projection
        Page<PostListItemResponseDTO> posts = studyPostRepository.findPostPage(pageRequest);

        return PageResponseDTO.from(posts); // Page -> PageResponseDTO 변환 (클라이언트에서 필요한값만 전달)
    }

    // 게시글_상세
    @Transactional(readOnly = true)
    public PostDetailResponseDTO detailFindPost(long id) {

        // 영속상태 Lazy
        StudyPost studyPost = studyPostRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        return PostDetailResponseDTO.from(studyPost); // DTO 변환 방식에 맞게
    }

    // 자료등록
    @Transactional
    public PostDetailResponseDTO createPost(PostCreateRequestDTO request) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        Member author = memberRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        StudyPost post = StudyPost.create(author, category, request.getTitle(), request.getBody(), request.getReferenceUrl());

        studyPostRepository.save(post);

        return PostDetailResponseDTO.from(post);
    }

    // 자료수정
    @Transactional
    public PostDetailResponseDTO updatePost(Long postId, PostUpdateRequestDTO request) {

        StudyPost studyPost = studyPostRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
        }

        studyPost.update(request.getTitle(), request.getBody(), category, request.getReferenceUrl());

        return PostDetailResponseDTO.from(studyPost);
    }

}