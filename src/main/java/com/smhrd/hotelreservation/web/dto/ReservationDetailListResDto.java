package com.smhrd.hotelreservation.web.dto;

import java.time.LocalDate;

import com.smhrd.hotelreservation.model.entity.ReservationDetails;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReservationDetailListResDto {
	private Long reservation_detail_id;
	private LocalDate date;
	private Long roomNumber;
	private String roomName;
	private String imageUrl;
	
	@Builder
	public ReservationDetailListResDto(ReservationDetails reservationDetails) {
		this.reservation_detail_id = reservationDetails.getId();
		this.date = reservationDetails.getDate();
		this.roomNumber = reservationDetails.getRooms().getRoomNumber();
		this.roomName = reservationDetails.getRooms().getRoomTypes().getRoomName();
		this.imageUrl = reservationDetails.getRooms().getRoomTypes().getImageUrl();
	}
	
}
