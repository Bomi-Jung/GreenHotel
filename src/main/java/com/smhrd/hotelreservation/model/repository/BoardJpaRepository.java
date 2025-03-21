package com.smhrd.hotelreservation.model.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smhrd.hotelreservation.model.entity.Board;

@Repository
public interface BoardJpaRepository extends JpaRepository<Board, Long> {


}
