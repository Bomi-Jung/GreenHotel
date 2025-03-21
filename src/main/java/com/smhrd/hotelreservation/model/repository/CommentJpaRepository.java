package com.smhrd.hotelreservation.model.repository;

import com.smhrd.hotelreservation.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentJpaRepository extends JpaRepository<Comment, Long> {
    // 게시글 ID로 댓글 목록을 조회하는 쿼리 추가
    @Query("SELECT c FROM Comment c WHERE c.postId.postId = :postId")
    List<Comment> findByPostId(@Param("postId") Long postId);
}
