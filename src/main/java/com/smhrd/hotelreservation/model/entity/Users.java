package com.smhrd.hotelreservation.model.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Users extends BaseEntity {

	@Id
	@Column(length=255, unique=true, nullable=false)//사용자아이디
	private String username;
	
	@Column(length=100, nullable=true)
	private String password;
	
	@Column(nullable=false)
	private String nickname;
	
	
	@Column(length = 100, nullable = false)
	String role; // 사용자 등급
	
	 @Enumerated(EnumType.STRING)
	 private AuthProvider authProvider; // 소셜 로그인 제공자

	
	

}