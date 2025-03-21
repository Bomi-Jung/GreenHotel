package com.smhrd.hotelreservation.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class RoomTypes extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="roomtype_id")
	private Long id;
	
	@Column(name="room_name")
	private String roomName;
	
	private Long price;
	
    @Column(name="image_url")
    private String imageUrl; 
    
	@Builder
	public RoomTypes(String roomName, Long price, String imageUrl) {
		this.roomName = roomName;
		this.price = price;
		this.imageUrl = imageUrl;
	}
	
	
}
