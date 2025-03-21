package com.smhrd.hotelreservation.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smhrd.hotelreservation.model.entity.RoomTypes;
import com.smhrd.hotelreservation.model.entity.Rooms;
import com.smhrd.hotelreservation.model.repository.RoomTypesJpaRepository;
import com.smhrd.hotelreservation.model.repository.RoomsJpaRepository;
import com.smhrd.hotelreservation.web.dto.RoomTypesResDto;
import com.smhrd.hotelreservation.web.dto.RoomTypesSaveReqDto;
import com.smhrd.hotelreservation.web.dto.RoomsDeleteReqDto;
import com.smhrd.hotelreservation.web.dto.RoomsResDto;
import com.smhrd.hotelreservation.web.dto.RoomsSaveReqDto;
import com.smhrd.hotelreservation.web.dto.RoomsUpdateReqDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoomsService {
	
	private final RoomTypesJpaRepository roomTypesJpaRepository;
	private final RoomsJpaRepository roomsJpaRepository;
	
	/**
	 * method	객실 등록
	 */
	@Transactional
	public Long addRoom(RoomsSaveReqDto requestDto) {
		RoomTypes roomTypes = roomTypesJpaRepository.findById(requestDto.getRoomType()).orElseThrow(() -> new IllegalArgumentException("해당 객실타입이 없습니다."));;
		return roomsJpaRepository.save(Rooms.builder()
				.roomNumber(requestDto.getRoomNumber())
				.roomTypes(roomTypes).build()).getId();
	}
	
	/**
	 * method	객실 수정
	 */
	public Long updateRoom(RoomsUpdateReqDto requestDto) {
		
		Rooms room = roomsJpaRepository.findById(requestDto.getRoomId()).orElseThrow(() -> new IllegalArgumentException("해당 객실이 없습니다."));
		
		RoomTypes roomTypes = roomTypesJpaRepository.findById(requestDto.getRoomTypeId()).orElseThrow(() -> new IllegalArgumentException("해당 객실 타입이 없습니다."));
		
		room.setRoomTypes(roomTypes);
		
		return roomsJpaRepository.save(room).getId();
	}
	
	/**
	 * method	특정 객실정보 조회
	 */
	@Transactional(readOnly = true)
	public Rooms findById(Long id) {
		return roomsJpaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 객실이 존재하지 않습니다."));
	}
	
	/**
	 * method	모든 객실정보 조회
	 */
	@Transactional(readOnly = true)
	public List<RoomsResDto> findAllRooms(){
		return roomsJpaRepository.findAll().stream().map((obj) -> new RoomsResDto(obj)).collect(Collectors.toList());
	}
	
	/**
	 * method	객실 삭제
	 */
	@Transactional
	public Long removeRoom(RoomsDeleteReqDto requestDto) {
		
		Rooms room = roomsJpaRepository.findById(requestDto.getId()).orElseThrow(() -> new IllegalArgumentException("해당 객실이 존재하지 않습니다."));
		
		roomsJpaRepository.delete(room);
		
		return room.getId();
	}
	
	
	
}