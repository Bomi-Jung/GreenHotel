package com.smhrd.hotelreservation.web.dto;

import com.smhrd.hotelreservation.model.entity.RoomTypes;
import com.smhrd.hotelreservation.model.entity.Rooms;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class RoomsResDto {
	private Long id;
	private Long roomNumber;
	private RoomTypes roomTypes;
	
	@Builder
	public RoomsResDto(Rooms room) {
		this.id = room.getId();
		this.roomNumber = room.getRoomNumber();
		this.roomTypes = room.getRoomTypes();
	}
	
}
