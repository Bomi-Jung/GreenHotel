package com.smhrd.hotelreservation.web.dto;

import com.smhrd.hotelreservation.model.entity.Users;

import lombok.Getter;

@Getter
public class SessionUser {
    private String username;
    private String nickname;
    private String role = "ROLE_USER";

    public SessionUser(Users user) {
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        System.out.println("SessionUser 생성 - Username: " + this.username + ", Nickname: " + this.nickname); // 로그 추가

    }
}