package com.smhrd.hotelreservation.service.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import com.smhrd.hotelreservation.model.entity.Users;
import com.smhrd.hotelreservation.model.repository.UsersJpaRepository;
/**로그인 사용자 정보를 가져오는 유틸 클래스**/

@Component
public class UserUtil {

	private final UsersJpaRepository usersJpaRepository;

	@Autowired
	public UserUtil(UsersJpaRepository usersJpaRepository) {
		this.usersJpaRepository = usersJpaRepository;
	}

	public String getCurrentUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
			return oauthToken.getPrincipal().getAttribute("email");
		} else if (authentication != null && authentication.isAuthenticated()) {
			return authentication.getName();
		}
		return null;
	}

	public String getCurrentNickname() {
		String username = getCurrentUsername();
		if (username != null) {
			Users user = usersJpaRepository.findByUsername(username);
			if (user != null) {
				return user.getNickname();
			}
		}
		return "익명";
	}

	// 현재 로그인한 사용자(Users 엔티티)를 가져옴
	public Users getCurrentUser() {
		String username = getCurrentUsername();
		return username != null ? usersJpaRepository.findByUsername(username) : null;
	}

}