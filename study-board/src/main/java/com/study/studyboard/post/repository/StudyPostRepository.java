package com.study.studyboard.post.repository;

import com.study.studyboard.post.dto.PostListItemResponseDTO;
import com.study.studyboard.post.entity.StudyPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudyPostRepository extends JpaRepository<StudyPost, Long> {

    /*
     * Repository 메서드의 마지막 파라미터로 Pageable을 받으면 Spring Data가 페이징처리를 해준다.
     * */

    @Query(value = """
            select new com.study.studyboard.post.dto.PostListItemResponseDTO(
              author.nickname,
              post.title,
              post.createdAt
            )
            from StudyPost post
            join post.author author
            """, countQuery = "select count(post) from StudyPost post")

        // Pageable : "몇 페이지를, 몇 개씩, 어떤 순서로 줄지"를 담은 페이지 요청 정보예
    Page<PostListItemResponseDTO> findPostPage(Pageable pageRequest);
}
