package com.smhrd.hotelreservation.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

import com.smhrd.hotelreservation.service.security.CustomOAuth2UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*시큐리티 설정*/

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	// 패스워드 인코더를 컨테이너에 빈으로 등록하는 메소드
	// 메소드에 @Bean을 붙이면, 반환 값인 인코더 객체가 스프링 컨테이너에 등록됨
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// 커스텀 HttpFirewall 설정
	@Bean
	public HttpFirewall defaultHttpFirewall() {
		DefaultHttpFirewall firewall = new DefaultHttpFirewall();
		firewall.setAllowUrlEncodedSlash(true); // //을 허용하도록 설정
		return firewall;
	}

	private final CustomOAuth2UserService customOAuth2UserService;

	@Autowired
	javax.sql.DataSource dataSource;

	// 사용자 인증을 처리하는 메소드
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		// 보안규칙 (메뉴별 접근제한 설정)
		http.authorizeHttpRequests().antMatchers("/register", "/", "/index", "/board", "/board/**").permitAll()
				.antMatchers("/assets/**", "/css/**", "/js/**").permitAll() // 리소스는 아무나 접근 가능
				// .antMatchers("/").authenticated() // 인증된 사용자이면 접근 	dj가능

				.antMatchers("/home/*","/reservation/*", "/reservationDetails/*","/admin/reservationList/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
				.antMatchers( "/home/reservationList/*").hasAuthority("ROLE_USER") // 사용자이면 접근 가능
				.antMatchers("/admin", "/admin/*", "/admin/room").hasAuthority("ROLE_ADMIN") // 회원 관리는 관리자이면 접근 가능
				.anyRequest().authenticated()// 다른 모든 요청은 인증되어야 함

				.and()// authorizeHttpRequests() 설정 종료, 다음 설정 시작
				.formLogin(); // 시큐리티가 제공하는 기본 로그인페이지 사용하기
		http.csrf().disable(); // csrf는 get을 제외하여 상태값을 위조(변경)할 수있는 post,put,delete 메소드를 막음
		http.logout().logoutSuccessUrl("/").and().securityContext()
				.securityContextRepository(new HttpSessionSecurityContextRepository());
		; // 로그아웃 처리


		http.formLogin().loginPage("/login") // 로그인 화면 주소
				.loginProcessingUrl("/login") // 로그인 처리 주소
				.permitAll().and().oauth2Login().loginPage("/login").userInfoEndpoint() // OAuth2 로그인 성공 후 사용자 정보를 처리할
																						// 서비스 등록
				.userService(customOAuth2UserService) // 사용자 정보를 처리할 서비스 설정
				.and().successHandler(new AuthenticationSuccessHandler() {

					@Override
					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
							Authentication authentication) throws IOException, ServletException {
						System.out.println("로그인 성공");
						response.sendRedirect("/");
					}

				});


		return http.build();
	}

}
