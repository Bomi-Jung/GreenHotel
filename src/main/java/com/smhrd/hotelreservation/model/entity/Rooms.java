package com.smhrd.hotelreservation.model.entity;



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
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Rooms extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="room_id")
	private Long id;
	
	private Long roomNumber;
	
	@ManyToOne
	@JoinColumn(name="roomtype_id")
	private RoomTypes roomTypes;
	
	@Builder
	public Rooms(Long roomNumber, RoomTypes roomTypes) {
		this.roomNumber = roomNumber;
		this.roomTypes = roomTypes;
	}

}
