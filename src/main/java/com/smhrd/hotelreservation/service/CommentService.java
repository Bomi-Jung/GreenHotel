package com.smhrd.hotelreservation.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smhrd.hotelreservation.model.entity.Board;
import com.smhrd.hotelreservation.model.entity.Comment;
import com.smhrd.hotelreservation.model.entity.Users;
import com.smhrd.hotelreservation.model.repository.BoardJpaRepository;
import com.smhrd.hotelreservation.model.repository.CommentJpaRepository;
import com.smhrd.hotelreservation.model.repository.UsersJpaRepository;
import com.smhrd.hotelreservation.web.dto.CommentDto;
import com.smhrd.hotelreservation.web.dto.UsersDto;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentJpaRepository commentJpaRepository;
    private final UsersJpaRepository usersJpaRepository;
    private final BoardJpaRepository boardJpaRepository;

    @Transactional
    public void saveComment(CommentDto commentDto) {
        // 1. 사용자명(username)으로 Users 객체 찾기
        Users username = usersJpaRepository.findByUsername(commentDto.getUsername().getUsername());
        System.out.println("username from DTO: " + commentDto.getUsername().getUsername());
        if (username == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        // 2. 닉네임으로 사용자 조회
        Users nickname = usersJpaRepository.findByUsername(commentDto.getNickname());  // 닉네임으로 찾기
        System.out.println("Nickname from DTO: " + commentDto.getNickname());
        if (nickname == null) {
            throw new IllegalArgumentException("사용자의 닉네임을 찾을 수 없습니다.");
        }

        // ✅ 2. 게시글 조회
        Board board = boardJpaRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // ✅ 3. 댓글 엔티티 생성
        Comment comment = Comment.builder()
                .postId(board)
                .userId(username)
                .nickname(nickname)
                .content(commentDto.getContent())
                .status(commentDto.getAdminOnly() ? Comment.Status.ANSWERED : Comment.Status.UNANSWERED)
                .build();

        // ✅ 4. 댓글 저장
        commentJpaRepository.save(comment);

        updateBoardStatus(board);
    }

    private void updateBoardStatus(Board board) {
        // 게시글의 댓글 중 하나라도 ANSWERED 상태가 있으면 게시글의 상태를 ANSWERED로 변경
        List<Comment> comments = commentJpaRepository.findByPostId(board.getPostId());
        boolean hasAnswered = comments.stream()
                .anyMatch(comment -> comment.getStatus() == Comment.Status.ANSWERED);  // 하나라도 ANSWERED인 경우

        if (hasAnswered) {
            board.setStatus(Board.Status.ANSWERED);
        } else {
            board.setStatus(Board.Status.UNANSWERED);
        }

        // 게시글 상태 변경 후 저장
        boardJpaRepository.save(board);

        // 게시글 상태 변경 후 저장
        boardJpaRepository.save(board);
    }


    // 특정 게시글의 댓글 목록을 가져오는 메서드
    public List<CommentDto> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentJpaRepository.findByPostId(postId);
        return comments.stream()
                .map(comment -> {
                    CommentDto dto = new CommentDto();
                    dto.setCommentId(comment.getCommentId());
                    dto.setPostId(comment.getPostId().getPostId());
                    dto.setContent(comment.getContent());
                    dto.setAdminOnly(comment.getAdminOnly());
                    dto.setStatus(comment.getStatus());

                    // UserDto 변환 추가
                    UsersDto userDto = new UsersDto();
                    userDto.setUsername(comment.getUserId().getUsername()); // username을 사용
                    userDto.setNickname(comment.getUserId().getNickname()); // nickname을 String으로 설정

                    dto.setUsername(userDto.getUsername());
                    dto.setNickname(userDto.getNickname());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // 댓글 삭제
    public boolean deleteComment(Long commentId) {
        Optional<Comment> commentOpt = commentJpaRepository.findById(commentId);

        if (commentOpt.isPresent()) {
            commentJpaRepository.delete(commentOpt.get());
            return true;
        }

        return false;
    }

}