package com.smhrd.hotelreservation.web.dto;

import java.util.Map;

import com.smhrd.hotelreservation.model.entity.Users;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {

	private String email;
	private String nickname;
	
	 @Builder
	    public OAuthAttributes(String email, String nickname) {
	        this.email = email;
	        this.nickname = nickname;
	    }

	    // 구글에서 받은 사용자 정보를 OAuthAttributes로 변환하는 메소드
	    public static OAuthAttributes ofGoogle(Map<String, Object> attributes) {
	        return OAuthAttributes.builder()
	                .email((String) attributes.get("email")) // 이메일을 가져옴
	                .nickname((String) attributes.get("name")) // 이름을 가져옴
	                .build();
	    }

	    // Users 엔티티로 변환하는 메소드
	    @Builder
	    public Users toEntity() {
	        return Users.builder()
	                .username(email) // 이메일을 username으로 사용
	                .nickname(nickname) // nickname으로 사용
	                .role("ROLE_USER") // 기본 역할
	                .build();
	    }
}
