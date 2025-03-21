package com.smhrd.hotelreservation.model.repository;

import java.util.List;

import com.smhrd.hotelreservation.model.entity.Reservations;
import com.smhrd.hotelreservation.model.entity.Users;

public interface ReservationsJpaRepositoryCustom {
	
	List<Reservations> findByUsers(Users user);

	Reservations findLatestByUsers(Users user);

}
