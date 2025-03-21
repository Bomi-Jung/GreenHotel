package com.smhrd.hotelreservation.model.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.smhrd.hotelreservation.model.entity.Reservations;
import com.smhrd.hotelreservation.model.entity.Users;

@Repository
public class ReservationsJpaRepositoryImpl implements ReservationsJpaRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Reservations> findByUsers(Users user) {
		return entityManager.createQuery("SELECT r FROM Reservations r WHERE r.users = :user", Reservations.class)
				.setParameter("user", user).getResultList();
	}

	@Override
	public Reservations findLatestByUsers(Users user) {
		List<Reservations> reservationsList = entityManager
				.createQuery("SELECT r FROM Reservations r WHERE r.users = :user ORDER BY r.id DESC",
						Reservations.class)
				.setParameter("user", user).setMaxResults(1).getResultList();
		return reservationsList.isEmpty() ? null : reservationsList.get(0);
	}
}