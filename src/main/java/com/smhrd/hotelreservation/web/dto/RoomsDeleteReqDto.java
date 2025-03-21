package com.smhrd.hotelreservation.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomsDeleteReqDto {
	private Long id;
	
	@Builder
	public RoomsDeleteReqDto(Long id) {
		this.id = id;
	}
}
