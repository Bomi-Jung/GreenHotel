package com.smhrd.hotelreservation.web.controller;

import com.smhrd.hotelreservation.model.entity.Board;
import com.smhrd.hotelreservation.model.entity.Board.Status;
import com.smhrd.hotelreservation.model.entity.Users;
import com.smhrd.hotelreservation.service.BoardService;
import com.smhrd.hotelreservation.service.CommentService;
import com.smhrd.hotelreservation.service.UsersService;
import com.smhrd.hotelreservation.service.util.UserUtil;
import com.smhrd.hotelreservation.web.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/board")
public class BoardController {

	private final BoardService boardService;
	private final UsersService usersService;
	private final UserUtil userUtil;
	private final CommentService commentService;

	@GetMapping("/list")
	public String getList(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean isLoggedIn = authentication != null && authentication.isAuthenticated();
		model.addAttribute("isLoggedIn", isLoggedIn);

		// UserUtil을 통해 현재 사용자 정보 가져오기
		String currentUsername = userUtil.getCurrentUsername();
		String nickname = userUtil.getCurrentNickname();

		// 사용자 정보를 모델에 추가
		model.addAttribute("username", currentUsername);
		model.addAttribute("nickname", nickname);

		// 게시판 목록 추가
		model.addAttribute("boards", boardService.getAllBoards());

		// 디버깅 로그
		System.out.println("인증 상태 확인: " + isLoggedIn);
		System.out.println("현재 SecurityContext 사용자: " + currentUsername);
		System.out.println("현재 사용자 닉네임: " + nickname);

		return "board/list";

	}

	// 게시글 상세 조회
	@GetMapping("/detail/{postId}")
	public String getBoardDetail(@PathVariable Long postId, Model model) {
		Board board = boardService.getBoardById(postId);
		List<CommentDto> comments = commentService.getCommentsByPostId(postId); // 댓글 목록 가져오기

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String nickname = authentication.getName();

		// 날짜 포맷터 생성
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedDate = board.getCreatedAt() != null ? board.getCreatedAt().format(formatter) : "날짜 없음";

		model.addAttribute("formattedDate", formattedDate);
		model.addAttribute("board", board);
		model.addAttribute("comments", comments);
		model.addAttribute("nickname", nickname);
		return "board/detail"; // board/detail.html로 매핑
	}

	// 게시글 작성 페이지로 이동
	@GetMapping("/create")
	public String createBoardForm(Model model, RedirectAttributes redirectAttributes) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("Authentication 객체: " + authentication); // 디버깅용 로그

		// 인증 여부 확인
		if (authentication == null || !authentication.isAuthenticated()
				|| authentication.getPrincipal().equals("anonymousUser")) {
			// 로그인 상태가 아닐 경우 리다이렉트
			redirectAttributes.addFlashAttribute("loginMessage", "로그인이 필요합니다.");
			return "redirect:/login";
		}

		// 로그인 상태일 경우 게시글 작성 페이지로 이동
		model.addAttribute("board", new Board());
		return "board/create"; // board/create.html로 매핑
	}

	// 게시글 작성 처리
	@PostMapping("/create")
	public String createBoard(@ModelAttribute Board board, RedirectAttributes redirectAttributes) {
		// 현재 로그인된 사용자 정보 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = null;

		// 소셜 로그인 사용자인지 확인
		if (authentication instanceof OAuth2AuthenticationToken) {
			OAuth2User oAuth2User = ((OAuth2AuthenticationToken) authentication).getPrincipal();
			currentUsername = oAuth2User.getAttribute("email"); // 소셜 로그인 이메일을 username으로 사용
		} else if (authentication != null && authentication.isAuthenticated()) {
			currentUsername = authentication.getName(); // 일반 로그인 사용자 이름
		}

		if (currentUsername == null) {
			System.out.println("로그인된 사용자 이름을 찾을 수 없습니다.");
			redirectAttributes.addFlashAttribute("loginMessage", "로그인이 필요합니다.");
			return "redirect:/login";
		}

		// UserService를 통해 User 객체를 가져오기
		Users currentUser = usersService.getUserByUsername(currentUsername);

		if (currentUser != null) {
			board.setUserId(currentUser); // 유저 ID 설정
			System.out.println("게시글 작성자 설정: " + currentUser.getNickname());
		} else {
			System.out.println("사용자 정보를 찾을 수 없습니다.");
			redirectAttributes.addFlashAttribute("loginMessage", "로그인된 사용자가 존재하지 않습니다.");
			return "redirect:/login";
		}

		board.setStatus(Status.UNANSWERED); // 답변미완료로 설정
		Board savedBoard = boardService.saveBoard(board);

		// 작성된 게시글의 ID를 리다이렉트 URL에 추가
		redirectAttributes.addAttribute("postId", savedBoard.getPostId());
		return "redirect:/board/detail/{postId}";
	}

	// 게시글 수정 페이지로 이동
	@GetMapping("/edit/{postId}")
	public String editBoardForm(@PathVariable Long postId, Model model) {
		Board board = boardService.getBoardById(postId);
		model.addAttribute("board", board);
		return "board/edit"; // Board/edit.html로 매핑
	}

	// 게시글 수정 처리
	@PostMapping("/edit/{postId}")
	public String updateBoard(@PathVariable Long postId, @ModelAttribute Board updatedBoard) {
		boardService.updateBoard(postId, updatedBoard);
		return "redirect:/board/list"; // 수정 후 목록 페이지로 리다이렉트
	}

	// 게시글 삭제 처리
	@PostMapping("/delete/{postId}")
	public String deleteBoard(@PathVariable Long postId) {
		boardService.deleteBoard(postId);
		return "redirect:/board/list"; // 삭제 후 목록 페이지로 리다이렉트
	}

}