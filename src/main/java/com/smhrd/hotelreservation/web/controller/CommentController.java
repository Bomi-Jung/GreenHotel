package com.smhrd.hotelreservation.web.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smhrd.hotelreservation.model.entity.Comment;
import com.smhrd.hotelreservation.model.repository.CommentJpaRepository;
import com.smhrd.hotelreservation.service.CommentService;
import com.smhrd.hotelreservation.web.dto.CommentDto;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentJpaRepository commentJpaRepository;


    @PostMapping("/add")
    public ResponseEntity<String> addComment(@RequestBody CommentDto commentDto) {
        commentService.saveComment(commentDto);
        return ResponseEntity.ok("댓글이 저장되었습니다.");
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long postId) {
        List<CommentDto> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }


    @PostMapping("/updateStatus/{commentId}")
    public ResponseEntity<String> updateCommentStatus(@PathVariable Long commentId, @RequestBody Map<String, Boolean> request) {
        Optional<Comment> commentOptional = commentJpaRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            boolean isAnswered = request.get("answered");

            // 상태 변경
            comment.updateStatus(isAnswered ? Comment.Status.ANSWERED : Comment.Status.UNANSWERED);
            commentJpaRepository.save(comment);

            return ResponseEntity.ok("댓글 상태가 변경되었습니다.");
        }
        return ResponseEntity.badRequest().body("해당 댓글을 찾을 수 없습니다.");
    }


}




