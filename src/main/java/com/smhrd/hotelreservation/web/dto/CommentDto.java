package com.smhrd.hotelreservation.web.dto;

import com.smhrd.hotelreservation.model.entity.Comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {

    private Long commentId;
    private Long postId; // 게시글 ID
    private UsersDto username; // 사용자 아이디
    private String nickname; // 사용자 닉네임
    private String content; // 댓글 내용
    private Boolean adminOnly; // 관리자만 답변한 상태인지 여부
    private Comment.Status status; // 답변 상태 (ANSWERED, UNANSWERED)



    public void setUsername(String username) {
        this.username = new UsersDto(username);
    }

    public void setAnswered(Boolean answered) {
        this.status = (answered) ? Comment.Status.ANSWERED : Comment.Status.UNANSWERED;
    }
}


