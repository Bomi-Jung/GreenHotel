package com.smhrd.hotelreservation.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReservationDetailSaveReqDto {
	Long id;
	
	@Builder
	public ReservationDetailSaveReqDto(Long id) {
		this.id = id;
	}
}
