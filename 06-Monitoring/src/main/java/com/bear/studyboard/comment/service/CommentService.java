package com.bear.studyboard.comment.service;

import com.bear.studyboard.comment.dto.CommentResponseDTO;
import com.bear.studyboard.comment.dto.CreateCommentRequestDTO;
import com.bear.studyboard.comment.entity.Comment;
import com.bear.studyboard.comment.repository.CommentRepository;
import com.bear.studyboard.common.dto.PageResponseDTO;
import com.bear.studyboard.common.error.BusinessException;
import com.bear.studyboard.common.error.ErrorCode;
import com.bear.studyboard.member.entity.Member;
import com.bear.studyboard.member.service.MemberService;
import com.bear.studyboard.post.entity.StudyPost;
import com.bear.studyboard.post.service.StudyPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final StudyPostService studyPostService;
    private final MemberService memberService;

    @Transactional
    public CommentResponseDTO createComment(long postId, CreateCommentRequestDTO request) {

        // 댓글이 달릴 게시글이 존재하는지 검증
        StudyPost post = studyPostService.findRequiredPost(postId);

        // 회원이 활성회원인지 검증
        Member author = memberService.requireActiveMember(request.getAuthorId());

        // 댓글 저장
        Comment comment = new Comment(post, author, request.getBody(), currentDateTime());
        Comment savedComment = commentRepository.save(comment);

        return CommentResponseDTO.from(savedComment, postId);
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<CommentResponseDTO> findComments(long postId, Pageable pageable) {

        // 댓글을 찾으려고하는 게시글이 존재하는지 검증
        studyPostService.requirePostExists(postId);

        // 페이징처리
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize);

        Page<Comment> comments = commentRepository.findPageByPostId(postId, pageRequest);

        Page<CommentResponseDTO> commentResponsePage = comments
                .map(comment -> CommentResponseDTO.from(comment, postId));

        return PageResponseDTO.from(commentResponsePage);
    }

    @Transactional
    public CommentResponseDTO updateComment(long commentId, String body) {

        // 수정하려고하는 댓글이 존재하는지 검증 후 반환
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        // 엔티티 수정 메서드를 만들어 변환 및 최근변경일자 정보 업데이트
        comment.revise(body, currentDateTime());

        StudyPost post = comment.getStudyPost();
        long postId = post.getId();

        return CommentResponseDTO.from(comment, postId);
    }

    @Transactional
    public void deleteComment(long commentId) {

        // 삭제된 댓글이 0개이면 해당 ID의 댓글이 존재하지 않는 것으로 판단한다.
        if (commentRepository.deleteByCommentId(commentId) == 0) {
            throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND);
        }
    }

    private static LocalDateTime currentDateTime() {
        return LocalDateTime.now();
    }
}
