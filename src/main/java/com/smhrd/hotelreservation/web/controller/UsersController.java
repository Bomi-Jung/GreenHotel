package com.smhrd.hotelreservation.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smhrd.hotelreservation.model.entity.Reservations;
import com.smhrd.hotelreservation.model.entity.Users;
import com.smhrd.hotelreservation.model.entity.UsersInfo;
import com.smhrd.hotelreservation.model.repository.UsersJpaRepository;
import com.smhrd.hotelreservation.service.ReservationsService;
import com.smhrd.hotelreservation.service.UsersService;
import com.smhrd.hotelreservation.web.dto.ReservationDetailListResDto;

@RestController
public class UsersController {

	private UsersService usersService;
    private ReservationsService reservationsService ;
    @Autowired
    private UsersJpaRepository usersJpaRepository;

    
    public UsersController(UsersService userService, ReservationsService reservationService) {
        this.usersService = userService;
        this.reservationsService = reservationService;
    }
   
   
    @GetMapping("/getUserAndReservationInfo")
    public UsersInfo getUserAndReservationInfo(String username) {
        if (username == null || username.equals("undefined")) {
            throw new RuntimeException("Invalid username: " + username);
        }

        Users user = usersJpaRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found for username: " + username);
        }

        Reservations reservationDetails = usersService.getReservationDetailsByUsername(username);
        if (reservationDetails == null) {
            throw new RuntimeException("ReservationDetails not found for username: " + username);
        }

        List<ReservationDetailListResDto> reservationDetailList = reservationDetails.getReservationDetails().stream()
                .map(ReservationDetailListResDto::new)
                .collect(Collectors.toList());
        Long totalPrice = reservationDetails.getTotalPrice();  // Reservations 엔티티에서 totalPrice를 가져옴.

        return new UsersInfo(user, reservationDetailList, totalPrice);  // UsersInfo 객체를 생성하고 반환.
    }
    
    
    @GetMapping("/getReservationDetailId")
    public Long getReservationDetailId(@RequestParam String username) {
        // UserService를 사용해서 사용자의 ReservationDetails를 가져옴
        Reservations reservationDetails = usersService.getReservationDetailsByUsername(username);

        // 가져온 ReservationDetails의 id를 반환
        return reservationDetails.getId();
    }
    
    @GetMapping("/getLoggedInUsername")
    public ResponseEntity<String> getLoggedInUsername() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(username);
    }
}

