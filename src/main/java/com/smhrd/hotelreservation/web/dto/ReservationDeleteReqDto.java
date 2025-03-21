package com.smhrd.hotelreservation.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReservationDeleteReqDto {
	Long id;
	
	@Builder
	public ReservationDeleteReqDto(Long id) {
		this.id = id;
	}
}
