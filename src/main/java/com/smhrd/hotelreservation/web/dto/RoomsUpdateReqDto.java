package com.smhrd.hotelreservation.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomsUpdateReqDto {
	private Long roomId;
	private Long roomTypeId;
	
	@Builder
	public RoomsUpdateReqDto(Long roomId, Long roomTypeId) {
		this.roomId = roomId;
		this.roomTypeId = roomTypeId;
	}
}
