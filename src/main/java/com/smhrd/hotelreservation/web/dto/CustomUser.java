package com.smhrd.hotelreservation.web.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**인증객체**/ 

public class CustomUser extends User{

	public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {

		super(username,password,authorities);

}
}
