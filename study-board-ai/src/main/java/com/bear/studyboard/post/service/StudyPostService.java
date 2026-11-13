package com.bear.studyboard.post.service;

import com.bear.studyboard.category.entity.Category;
import com.bear.studyboard.category.service.CategoryService;
import com.bear.studyboard.common.dto.PageResponseDTO;
import com.bear.studyboard.common.error.BusinessException;
import com.bear.studyboard.common.error.ErrorCode;
import com.bear.studyboard.member.entity.Member;
import com.bear.studyboard.member.service.MemberService;
import com.bear.studyboard.post.dto.CreatePostRequestDTO;
import com.bear.studyboard.post.dto.PostDetailResponseDTO;
import com.bear.studyboard.post.dto.PostListItemResponseDTO;
import com.bear.studyboard.post.dto.UpdatePostRequestDTO;
import com.bear.studyboard.post.entity.StudyPost;
import com.bear.studyboard.post.repository.StudyPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudyPostService {

    private final StudyPostRepository studyPostRepository;
    private final MemberService memberService;
    private final CategoryService categoryService;

    @Transactional
    public PostDetailResponseDTO createPost(CreatePostRequestDTO request) {

        // requireActiveMember: 이 회원이 지금 활동할 수 있는 회원인지 두 단계로 확인한다
        Member author = memberService.requireActiveMember(request.getAuthorId());

        Category category = categoryService.findRequiredCategory(request.getCategoryId());

        StudyPost post = new StudyPost(author, category, request.getTitle(), request.getBody(),
                request.getReferenceUrl(), currentDateTime());

        StudyPost savedPost = studyPostRepository.save(post);

        return PostDetailResponseDTO.from(savedPost);
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<PostListItemResponseDTO> findPosts(Pageable pageable) {
        // 최신순으로 조회하고 작성 시각이 같으면 게시글 ID로 순서를 정한다.
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt", "id");

        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        Page<PostListItemResponseDTO> posts = studyPostRepository.findPostPage(pageRequest);

        return PageResponseDTO.from(posts);
    }

    @Transactional(readOnly = true)
    public PostDetailResponseDTO findPost(long postId) {

        StudyPost post = findRequiredPost(postId);

        return PostDetailResponseDTO.from(post);
    }

    @Transactional
    public PostDetailResponseDTO updatePost(long postId, UpdatePostRequestDTO request) {
        StudyPost post = findRequiredPost(postId);
        // 생략하거나 null로 전달한 값은 기존 값을 유지한다.
        Category category = post.getCategory();

        if (request.getCategoryId() != null) {
            category = categoryService.findRequiredCategory(request.getCategoryId());
        }

        String title = post.getTitle();
        if (request.getTitle() != null) {
            title = request.getTitle();
        }

        String body = post.getBody();
        if (request.getBody() != null) {
            body = request.getBody();
        }

        String referenceUrl = post.getReferenceUrl();
        if (request.getReferenceUrl() != null) {
            referenceUrl = request.getReferenceUrl();
        }

        post.revise(category, title, body, referenceUrl, currentDateTime());
        return PostDetailResponseDTO.from(post);
    }

    // 반복되는코드 분리시킨것!
    @Transactional(readOnly = true)
    public StudyPost findRequiredPost(long postId) {
        Optional<StudyPost> post = studyPostRepository.findById(postId);
        return post.orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public void requirePostExists(long postId) {
        if (!studyPostRepository.existsById(postId)) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }
    }

    private static LocalDateTime currentDateTime() {
        return LocalDateTime.now();
    }
}
