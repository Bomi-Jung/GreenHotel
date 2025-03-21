package com.smhrd.hotelreservation.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.smhrd.hotelreservation.service.UsersService;
import com.smhrd.hotelreservation.web.dto.CustomUser;
import com.smhrd.hotelreservation.web.dto.UsersDto;

/**로그인 시 사용자 정보 조회 후 인증객체로 변환**/

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UsersService service;

	// 사용자 아이디를 기반으로 인증객체를 생성하여 저장하는 메소드
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		System.out.println("login id : " + username);

		// 아이디로 실제 회원 정보 가져오기
		UsersDto dto = service.read(username);

		if (dto == null) {
			throw new UsernameNotFoundException(""); // 사용자 정보가 없다면 에러 발생

		} else {
			return new CustomUser(dto.getUsername(), dto.getPassword(),
					Collections.singletonList(new SimpleGrantedAuthority(dto.getRole()))); // dto를 인증객체로 변환하여 저장
		}
	}

}
