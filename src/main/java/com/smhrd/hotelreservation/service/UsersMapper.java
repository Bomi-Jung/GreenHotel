package com.smhrd.hotelreservation.service;

import com.smhrd.hotelreservation.model.entity.Users;
import com.smhrd.hotelreservation.web.dto.UsersDto;

public class UsersMapper {
	
	//엔티티를 dto로 변환하는 메소드
    public static UsersDto entityToDto(Users entity) {
        UsersDto dto = new UsersDto();    
        dto.setUsername(entity.getUsername());
        dto.setPassword(entity.getPassword());
        dto.setNickname(entity.getNickname());
        dto.setRole(entity.getRole());
        return dto;
    }
    
   

    //dto를 엔티티로 변환하는 메소드
    public static Users dtoToEntity(UsersDto dto) {
        Users entity = new Users();       
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setNickname(dto.getNickname());
        entity.setRole(dto.getRole());
        return entity;
    }
}