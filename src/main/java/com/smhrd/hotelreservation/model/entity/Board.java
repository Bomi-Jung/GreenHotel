package com.smhrd.hotelreservation.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "board")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	Long postId; // 게시글 번호

	@ManyToOne
	@JoinColumn(name = "user_id")
	Users userId; // 작성자 아이디

	@Column(length = 100, nullable = false)
	String title; // 제목

	@Column(length = 2000, nullable = false)
	String content; // 내용

	@Column(name = "view_count", nullable = false)
	int viewCount = 0; // 조회수

	@Column
	boolean is_notice; // 공지글

	@Transient
	private String formattedDate;

	// 날짜 포맷팅 메소드
	@PostLoad
	public void formatDate() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		this.formattedDate = this.createdAt != null ? this.createdAt.format(formatter) : "날짜 없음";
	}

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	Status status; // 상태(답변완료,답변미완료)

	public enum Status {
		ANSWERED, UNANSWERED
	}

	// 닉네임을 가져오는 메서드
	public String getNickname() {
		return userId != null ? userId.getNickname() : null;
	}

}
