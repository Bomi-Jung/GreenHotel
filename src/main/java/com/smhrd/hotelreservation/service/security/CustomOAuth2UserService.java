package com.smhrd.hotelreservation.service.security;

import java.util.Collections;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.smhrd.hotelreservation.model.entity.AuthProvider;
import com.smhrd.hotelreservation.model.entity.Users;
import com.smhrd.hotelreservation.model.repository.UsersJpaRepository;
import com.smhrd.hotelreservation.web.dto.SessionUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService{

	  private final UsersJpaRepository usersJpaRepository;
	  private final HttpSession httpSession;
	  
	  @Override
	    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
	        // 기본적으로 OAuth2 사용자 정보 가져오기
	        OAuth2User oAuth2User = super.loadUser(userRequest);
	        String email = oAuth2User.getAttribute("email");
	        String name = oAuth2User.getAttribute("name");
	        System.out.println("구글 로그인 사용자 email: " + email + ", name: " + name); // 디버그 로그
	        

	        // 구글 로그인에서 유저 엔티티를 확인하거나 없으면 생성
	        Users user = usersJpaRepository.findByUsername(email);
	        if (user == null) {
	            user = new Users();
	            user.setUsername(email);
	            user.setNickname(name);
	            user.setRole("ROLE_USER");
	            user.setAuthProvider(AuthProvider.GOOGLE); // 구글 로그인으로 설정
	            user.setPassword(null); // 소셜 로그인은 비밀번호가 필요 없음
	            usersJpaRepository.save(user);
	        } else if (user.getAuthProvider() != AuthProvider.GOOGLE) {
	            throw new IllegalStateException("이미 다른 방식으로 가입된 사용자입니다.");
	        }
	        
	        
	        SessionUser sessionUser = new SessionUser(user);
	        System.out.println("세션 사용자 nickname: " + sessionUser.getNickname()); // 디버그 로그
	        httpSession.setAttribute("user", sessionUser);

	       
	        // 이메일을 `Principal`의 이름으로 설정
	        return new DefaultOAuth2User(
	            Collections.singleton(new SimpleGrantedAuthority(user.getRole())),
	            oAuth2User.getAttributes(),
	            "email"
	        );
	    }
	}

