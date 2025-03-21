package com.smhrd.hotelreservation.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smhrd.hotelreservation.model.entity.Reservations;
import com.smhrd.hotelreservation.model.entity.Users;


public interface ReservationsJpaRepository extends JpaRepository<Reservations, Long> , ReservationsJpaRepositoryCustom{
	 
	List<Reservations> findByUsers(Users user);
	
	@Query("SELECT r FROM Reservations r WHERE r.id = :reservationId AND r.users.username = :username")
	Optional<Reservations> findByReservationIdAndUsername(Long reservationId, String username);

	
	@Query("select r from Reservations r where  r.impUid = :impUid")
    Reservations findByImpUid(@Param("impUid") String impUid);
	
	@Query("select r from Reservations r where r.users.username = :name")
	Reservations findByUsername(@Param("name") String username);
	
	
}