package com.smhrd.hotelreservation.web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersDto {

    private Long id;

    private String username;

    private String password;

    private String nickname;

    String role; //사용자 등급 추가 (사용자:ROLE_USER, 관리자:ROLE_ADMIN)

    // 문자열을 받는 생성자 추가
    public UsersDto(String username) {
        this.username = username;
    }

}