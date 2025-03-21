package com.smhrd.hotelreservation.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smhrd.hotelreservation.model.entity.RoomTypes;

public interface RoomTypesJpaRepository extends JpaRepository<RoomTypes, Long>{
	Optional<RoomTypes> findByRoomName(String roomName);
}
