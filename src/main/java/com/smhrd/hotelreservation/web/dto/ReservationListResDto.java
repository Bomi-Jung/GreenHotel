package com.smhrd.hotelreservation.web.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.smhrd.hotelreservation.model.entity.ReservationDetails;
import com.smhrd.hotelreservation.model.entity.Reservations;
import com.smhrd.hotelreservation.model.entity.Users;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ReservationListResDto {
	
	private String username;
	private LocalDate checkInDate;
	private LocalDate checkOutDate;
	private Long roomNumber;
	private Long reservation_id;
	private LocalDateTime CreatedAt;
	private Long totalPrice;
	public String dateRange;  
	private String imageUrl;
	
	@Builder
	public ReservationListResDto(Users user, Reservations reservation, List<ReservationDetails> l,String dateRange, String imageUrl) {
		this.username = user.getUsername();
		this.checkInDate  = l.get(0).getDate();
		this.checkOutDate = l.size() == 1 ? l.get(0).getDate() : l.get(l.size() - 1).getDate();
		this.roomNumber = reservation.getReservationDetails().get(0).getRooms().getRoomNumber();
		this.reservation_id = reservation.getId();
		this.CreatedAt = reservation.getCreatedAt();
		this.totalPrice = reservation.getTotalPrice();
		this.dateRange = dateRange; 
		this.imageUrl = imageUrl;
	}
	
	 // 체크인 날짜와 체크아웃 날짜를 받아오는 메소드 추가
    public void setCheckInAndOutDates(LocalDate checkInDate, LocalDate checkOutDate) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

}