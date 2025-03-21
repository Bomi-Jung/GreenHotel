package com.smhrd.hotelreservation.web.controller;

import java.math.BigDecimal;
import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smhrd.hotelreservation.service.ReservationsService;
import com.smhrd.hotelreservation.web.dto.ReservationDeleteReqDto;
import com.smhrd.hotelreservation.web.dto.ReservationDetailSaveReqDto;
import com.smhrd.hotelreservation.web.dto.ReservationSaveReqDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ReservationsController {

	private final ReservationsService reservationsService;

	// 결제 요청 시 impUid 저장
	 @ResponseBody
	@PostMapping("/saveImpUidForReservation")
	public ResponseEntity<?> saveImpUidForReservation(@RequestParam String username, @RequestParam String impUid,
			@RequestParam BigDecimal paymentAmount, // 결제 금액 추가
			@RequestParam String paymentStatus) {

		System.out.println("impUid: " + impUid);
		System.out.println("paymentAmount: " + paymentAmount);
		System.out.println("paymentStatus: " + paymentStatus);
		try {
			reservationsService.saveImpUidForReservation(username, impUid, paymentAmount, paymentStatus);
			return ResponseEntity.ok("impUid 저장 완료");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("impUid 저장 실패");
		}
	}

	// post 예약등록
	 @ResponseBody
	@PostMapping("/home/reservation/add")
	public Long reservation(@RequestBody ReservationSaveReqDto requestDto, Principal principal) {

		System.out.println(requestDto);
		// 사용자 정보를 Users 객체로 가져오기
		String userid = principal.getName();
		// 사용자가 없으면 예외 처리 추가 필요

		log.info("view to controller with addReservation");

		// saveReservation 호출 시 Users 객체 전달
		return reservationsService.saveReservation(requestDto, userid);
	}

	// 예약내역
	@GetMapping("/admin/reservationList")
	public String reservationList(Model model) {
		log.info("예약내역 이동...");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
		boolean isUser = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"));

		model.addAttribute("isAdmin", isAdmin);
		model.addAttribute("isUser", isUser);
		model.addAttribute("currentUsername", auth.getName());
		model.addAttribute("reservationList", reservationsService.findAll());

		return "reservationList";
	}

	// delete 관리자 예약 취소 ( 삭제 )
	 @ResponseBody
	@DeleteMapping("/admin/reservation/remove")
	public Long deleteReservation(@RequestBody ReservationDeleteReqDto requestDto) {
		log.info("view to controller with deleteReservation");
		return reservationsService.deleteReservation(requestDto);
	}

	// delete 관리자 상세예약 취소 ( 삭제 )
	 @ResponseBody
	@DeleteMapping("/admin/reservationdetails/remove")
	public Long deleteReservationDetails(@RequestBody ReservationDetailSaveReqDto requestDto) {
		log.info("view to controller with deleteReservationDetails");
		return reservationsService.deleteReservationDetails(requestDto);
	}

}
