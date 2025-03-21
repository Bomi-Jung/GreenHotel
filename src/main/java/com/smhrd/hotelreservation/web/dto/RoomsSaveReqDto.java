package com.smhrd.hotelreservation.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomsSaveReqDto {
	private Long roomNumber;
	private Long roomType;
	
	@Builder
	public RoomsSaveReqDto(Long roomNumber, Long roomType) {
		this.roomNumber = roomNumber;
		this.roomType = roomType;
	}
	
}
