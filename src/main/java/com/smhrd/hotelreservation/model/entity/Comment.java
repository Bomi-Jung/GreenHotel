package com.smhrd.hotelreservation.model.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tbl_comment")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	Long commentId; // 댓글 번호

	@ManyToOne
	@JoinColumn(name = "post_id")
	Board postId; // 게시글 번호

	@ManyToOne
	@JoinColumn(name = "user_id")
	Users userId; // 작성자아이디

	@ManyToOne
	@JoinColumn(name = "nickname")
	Users nickname; // 작성자

	@Column(length = 1000, nullable = false)
	String content; // 댓글내용

	@Builder.Default
	@Column(nullable = true)
	Boolean adminOnly = false;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	Status status = Status.UNANSWERED; // 답변 상태 (ANSWERED, UNANSWERED)

	public enum Status {
		ANSWERED, UNANSWERED
	}

	public void updateStatus(Status status) {
		this.status = status;


		// 댓글의 상태에 따라 게시글의 상태를 변경
		if (status == Status.ANSWERED) {
			postId.setStatus(Board.Status.ANSWERED);
		} else {
			postId.setStatus(Board.Status.UNANSWERED);
		}
	}
}
