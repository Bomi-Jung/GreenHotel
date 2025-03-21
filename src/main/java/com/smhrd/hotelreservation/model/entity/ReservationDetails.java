package com.smhrd.hotelreservation.model.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class ReservationDetails extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="reservation_detail_id")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="reservation_id", insertable=false, updatable=false)
	private Reservations reservations;
	
	@ManyToOne
	@JoinColumn(name="room_id")
	private Rooms rooms;
	
	// LocalDate : 연,월,일 정보만 포함
	private LocalDate date;
	
	@Builder
	public ReservationDetails(Rooms rooms, LocalDate date, Long id) {
		this.rooms = rooms;
		this.date = date;
		this.id = id;
	}
	
}
