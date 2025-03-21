package com.smhrd.hotelreservation.service;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import com.smhrd.hotelreservation.model.entity.AuthProvider;
import com.smhrd.hotelreservation.model.entity.Reservations;
import com.smhrd.hotelreservation.model.entity.Users;
import com.smhrd.hotelreservation.model.repository.ReservationDetailsJpaRepository;
import com.smhrd.hotelreservation.model.repository.ReservationsJpaRepository;
import com.smhrd.hotelreservation.model.repository.UsersJpaRepository;
import com.smhrd.hotelreservation.service.security.PasswordGenerator;
import com.smhrd.hotelreservation.web.dto.SessionUser;
import com.smhrd.hotelreservation.web.dto.UsersDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Getter
@Setter

public class UsersService {

	private final UsersJpaRepository usersJpaRepository;
	private final ReservationDetailsJpaRepository reservationDetailsJpaRepository;
	private final ReservationsJpaRepository reservationsJpaRepository;
	private final HttpSession httpSession;

	@Autowired
	PasswordEncoder passwordEncoder;

	public Users getUserByUsername(String username) {
		return usersJpaRepository.findByUsername(username);
	}

	public Reservations getReservationDetailsByUsername(String username) {
		Users users = usersJpaRepository.findByUsername(username);
		return reservationsJpaRepository.findLatestByUsers(users);
	}

	public void saveUser(Users user) {
		usersJpaRepository.save(user);
	}

	public String save() {
		log.info("usersService method save start...");

		// 현재 사용자의 정보 가져옴
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		// 여기서 username을 사용하여 해당 사용자를 찾거나 권한을 확인
		Users users = usersJpaRepository.findByUsername(username);

		// 사용자가 없으면 새로 생성
		if (users == null) {
			// 새로운 사용자를 생성하고 필수 필드를 설정
			String generatedPassword = PasswordGenerator.generateRandomPassword(); // 랜덤한 비밀번호 생성
			users = Users.builder().username(username).password(passwordEncoder.encode(generatedPassword))
					.nickname("USER").role("ROLE_USER").build();
			users = usersJpaRepository.save(users);
		}

		return users.getUsername();
	}

	// 로그인 확인 여부 메소드
    public boolean login(String username, String password) {
        // 사용자 정보를 데이터베이스에서 조회
        Users user = usersJpaRepository.findByUsername(username);

        // 사용자가 존재하고, 입력한 비밀번호가 일치하면 인증 성공
        if (user != null && user.getAuthProvider() == AuthProvider.LOCAL && passwordEncoder.matches(password, user.getPassword())) {
            return true;
        }

        // 구글 사용자에 대해서는 비밀번호를 확인하지 않음
        if (user != null && user.getAuthProvider() == AuthProvider.GOOGLE) {
            return true; // 구글 사용자는 자동 인증
        }

        // 그렇지 않으면 인증 실패
        return false;
    }

	// 해당 회원의 존재 여부를 확인하는 메소드
	public UsersDto read(String username) {
		Users user = usersJpaRepository.findByUsername(username);
		if (user != null) {
			return UsersMapper.entityToDto(user);
		} else {
			return null;
		}
	}
	
	public boolean registerUser(String username, String nickname, String password, AuthProvider authProvider) {
	    Users user = usersJpaRepository.findByUsername(username);
	    if (user != null) {
	        System.out.println("이미 존재하는 사용자입니다.");
	        return false; // 이미 존재하면 false 반환
	    }

	    Users newUser = Users.builder()
	        .username(username)
	        .nickname(nickname)
	        .role("ROLE_USER")
	        .authProvider(authProvider)
	        .password(authProvider == AuthProvider.GOOGLE ? null : passwordEncoder.encode(password)) // 비밀번호 설정
	        .build();

	    usersJpaRepository.save(newUser);
	    return true; // 성공적으로 등록되면 true 반환
	}




	//**소셜로그인 사용자의 nickname을 가져오는 메소드라 사용시 주의 
	public String getCurrentUsername() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication == null || !authentication.isAuthenticated()) {
	        System.out.println("Authentication is null or not authenticated");
	        return null;
	    }

	    if (authentication instanceof OAuth2AuthenticationToken) {
	        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");

	        if (sessionUser != null) {
	            System.out.println("Social login user: " + sessionUser.getUsername());
	            return sessionUser.getNickname() != null ? sessionUser.getNickname() : sessionUser.getUsername();
	        }
	    }

	    String currentUsername = authentication.getName();
	    Users user = usersJpaRepository.findByUsername(currentUsername);

	    if (user != null) {
	        System.out.println("Normal login user: " + user.getUsername());

	        return user.getNickname() != null ? user.getNickname() : user.getUsername();
	    }
	    System.out.println("No user found");
	    return null;
	}
	
	}

	