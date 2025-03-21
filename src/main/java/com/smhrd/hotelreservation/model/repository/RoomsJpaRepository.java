package com.smhrd.hotelreservation.model.repository;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smhrd.hotelreservation.model.entity.Reservations;
import com.smhrd.hotelreservation.model.entity.RoomTypes;
import com.smhrd.hotelreservation.model.entity.Rooms;
import com.smhrd.hotelreservation.model.entity.Users;

@Repository
public interface RoomsJpaRepository extends JpaRepository<Rooms, Long> {
	List<Rooms> findByRoomTypes(RoomTypes roomType);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT r FROM Rooms r WHERE r.id = :roomId")
	Rooms findByIdWithLock(@Param("roomId") Long roomId);

}
