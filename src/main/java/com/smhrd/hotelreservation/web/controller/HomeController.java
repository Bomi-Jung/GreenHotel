package com.smhrd.hotelreservation.web.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smhrd.hotelreservation.model.entity.Reservations;
import com.smhrd.hotelreservation.service.ReservationsService;
import com.smhrd.hotelreservation.service.RoomTypesService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {
	
	private final ReservationsService reservationsService;
	private final RoomTypesService roomTypesService;
	
	// reservation(예약하기)
	@GetMapping("/home/reservation")
	public String reservation(Model model) {
		log.info("예약하기 이동...");
		model.addAttribute("roomTypes", roomTypesService.findAll());
		return "reservation";
	}
	
	
	@GetMapping("/home/reservationList/detail/{reservationId}")
	public ResponseEntity<?> getReservationDetails(@PathVariable("reservationId") Long reservationId, Principal principal) {
	    String username = principal.getName();

	    try {
	        Reservations reservations = reservationsService.findByReservationIdAndUsername(reservationId, username);
	        System.out.println("Reservation username: " + reservations.getUsers().getUsername()); // 예약 사용자 이름 콘솔에 출력
	        return new ResponseEntity<>(reservations.getReservationDetails(), HttpStatus.OK);
	    } catch (RuntimeException e) {
	        return new ResponseEntity<>("No reservation detail found for this id and username", HttpStatus.NOT_FOUND);
	    }
	}
	
	
	// 예약내역
	@GetMapping("/home/reservationList")
	public String reservationList(Model model, Authentication authentication) {
	    log.info("예약내역 이동...");
	    String username = authentication.getName(); // 현재 로그인한 사용자의 아이디를 가져옴

	    // 현재 로그인한 사용자의 예약 내역만 가져옴
	    model.addAttribute("reservationList", reservationsService.findAllByUser(username));
	    
	    return "reservationList";
	}
	
	// 예약 상세 내역
	@GetMapping("/home/reservationList/detail")
	public String reservationDetail(Model model, @RequestParam("reservation_id") String reservationId, RedirectAttributes redirectAttributes) {
		log.info("예약상세 이동...");
		model.addAttribute("reservtionDetail", reservationsService.findOne(Long.parseLong(reservationId)));
		return "reservationDetailsList";
	}
	

}
