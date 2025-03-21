package com.smhrd.hotelreservation.web.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import com.smhrd.hotelreservation.model.entity.ReservationDetails;
import com.smhrd.hotelreservation.model.entity.Reservations;
import com.smhrd.hotelreservation.model.entity.Users;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReservationDetailResDto extends ReservationListResDto{
	
	private String roomName;
	private Long totalPrice;
	private String roomImageUrl;

	public ReservationDetailResDto(Users user, Reservations reservation, List<ReservationDetails> l,String dateRange) {
		
		this.roomName = reservation.getReservationDetails().get(0).getRooms().getRoomTypes().getRoomName();
        this.roomImageUrl = reservation.getReservationDetails().get(0).getRooms().getRoomTypes().getImageUrl(); // imageUrl을 가져옵니다.
		this.totalPrice = reservation.getTotalPrice();
		this.dateRange = formatDateRange(dateRange);
	}
	private String formatDateRange(String dateRange) {
	    // dateRange를 원하는 형식으로 변환하는 로직을 작성.
	    // 예를 들어, "yyyy-MM-dd ~ yyyy-MM-dd" 형식을 "MMM dd, yyyy - MMM dd, yyyy" 형식으로 변경하려면 아래와 같이 작성할 수 있음.
	    String[] dates = dateRange.split(" ~ ");
	    LocalDate startDate = LocalDate.parse(dates[0]);
	    LocalDate endDate = LocalDate.parse(dates[1]);
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);
	    return formatter.format(startDate) + " - " + formatter.format(endDate);
	}
}