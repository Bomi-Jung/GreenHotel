package com.smhrd.hotelreservation.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smhrd.hotelreservation.model.entity.Users;

@Repository
public interface UsersJpaRepository extends JpaRepository<Users, String>{
	@Query("select u from Users u where u.username = :name")
	Users findByUsername(@Param("name") String username);
	
	
}


