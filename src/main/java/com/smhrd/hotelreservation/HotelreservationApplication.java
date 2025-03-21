package com.smhrd.hotelreservation;

import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;



@SpringBootApplication
@EnableJpaAuditing
@ComponentScan(basePackages = "com.smhrd.hotelreservation")
//컴포넌트를 찾아서 빈으로 등록하기 위해 지정된 패키지와 하위패키지을 스캔하는 기능 (굳이 필요하지 않은 기능)
public class HotelreservationApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelreservationApplication.class, args);
	}

	
	@Bean
	public AuditorAware<String> auditorProvider() {
	    return () -> {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        return Optional.ofNullable(authentication != null ? authentication.getName() : null);
	    };
}
}
