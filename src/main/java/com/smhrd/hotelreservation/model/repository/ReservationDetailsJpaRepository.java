package com.smhrd.hotelreservation.model.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smhrd.hotelreservation.model.entity.ReservationDetails;
import com.smhrd.hotelreservation.model.entity.Users;

@Repository
public interface ReservationDetailsJpaRepository extends JpaRepository<ReservationDetails, Long> {
	
	List<ReservationDetails> findAllByDateBetween(LocalDate start, LocalDate end);

	List<ReservationDetails> findAllByReservationsId(long l);
	

}
