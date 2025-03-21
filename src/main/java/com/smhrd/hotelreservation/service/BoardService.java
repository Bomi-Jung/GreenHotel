package com.smhrd.hotelreservation.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.smhrd.hotelreservation.model.entity.Board;
import com.smhrd.hotelreservation.model.repository.BoardJpaRepository;

@Service
public class BoardService {

	private final BoardJpaRepository boardJpaRepository;

	public BoardService(BoardJpaRepository boardJpaRepository) {
		this.boardJpaRepository = boardJpaRepository;
	}

	public List<Board> getAllBoards() {
		// 게시글 번호(postId)를 기준으로 내림차순 정렬
		return boardJpaRepository.findAll(Sort.by(Sort.Order.desc("postId")));
	}

	public Board getBoardById(Long postId) {
		Board board = boardJpaRepository.findById(postId).orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
		board.setViewCount(board.getViewCount() + 1); // 조회수 증가
		boardJpaRepository.save(board);
		return board;
	}

	public Board saveBoard(Board board) {
		return boardJpaRepository.save(board);
	}

	public void updateBoard(Long postId, Board updatedBoard) {
		Board board = getBoardById(postId);
		board.setTitle(updatedBoard.getTitle());
		board.setContent(updatedBoard.getContent());
		boardJpaRepository.save(board);
	}

	public void deleteBoard(Long postId) {
		boardJpaRepository.deleteById(postId);
	}
}
