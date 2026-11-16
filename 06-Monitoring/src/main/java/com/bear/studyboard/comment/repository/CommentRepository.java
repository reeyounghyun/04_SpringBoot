package com.bear.studyboard.comment.repository;

import com.bear.studyboard.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = """
            select c
            from Comment c
            join fetch c.author
            where c.studyPost.id = :postId
            order by c.createdAt asc, c.id asc
            """, countQuery = """
            select count(c.id)
            from Comment c
            where c.studyPost.id = :postId
            """)
    Page<Comment> findPageByPostId(@Param("postId") long postId, Pageable pageable);

    @Modifying
    @Query("delete from Comment c where c.id = :commentId")
    int deleteByCommentId(@Param("commentId") long commentId);
}
