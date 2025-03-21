 package com.smhrd.hotelreservation.web.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.smhrd.hotelreservation.service.RoomsService;
import com.smhrd.hotelreservation.web.dto.RoomsDeleteReqDto;
import com.smhrd.hotelreservation.web.dto.RoomsSaveReqDto;
import com.smhrd.hotelreservation.web.dto.RoomsUpdateReqDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RoomsController {

	private final RoomsService roomService;
	
	@PostMapping("/admin/room/add")
	public Long addRoom(@RequestBody RoomsSaveReqDto requestDto) {
		log.info("view to controller with addRoom");
		return roomService.addRoom(requestDto);
	}
	
	@PutMapping("/admin/room/update")
	public Long updateRoom(@RequestBody RoomsUpdateReqDto requestDto) {
		return roomService.updateRoom(requestDto);
	}
	
	@DeleteMapping("/admin/room/remove")
	public Long removeRoom(@RequestBody RoomsDeleteReqDto requestDto) {
		log.info("view to controller with removeRoomtype");
		Long result;
			
		try {
			result = roomService.removeRoom(requestDto);
		} catch (Exception e) {
			// 객실 삭제중 이미 그 객실을 예약한 사람이 있을경우 -1L 반환
			result= -1L;
		}
		return result;
	}
	
}
