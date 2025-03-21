package com.smhrd.hotelreservation.config;

import com.smhrd.hotelreservation.model.entity.Users;
import com.smhrd.hotelreservation.model.repository.UsersJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class DataLoader implements CommandLineRunner {

    
    @Autowired
    UsersJpaRepository usersJpaRepository;



    @Autowired
    private PasswordEncoder passwordEncoder; // 패스워드 인코더

    @Override
    public void run(String... args) throws Exception {
        // 초기 데이터 로딩 작업 수행
        
        // 관리자 계정 정보 입력
        String adminUsername = "admin";
        String adminPassword = "1234"; 
        String nickname = "관리자";
        String adminRole = "ROLE_ADMIN";
        

     // 이미 관리자 계정이 존재하는지 확인
        Users existingAdmin = usersJpaRepository.findByUsername(adminUsername);
        if (existingAdmin == null) {
            // 관리자 계정이 존재하지 않으면 새로운 계정 생성
            Users admin = Users.builder()
                .username(adminUsername)
                .password(passwordEncoder.encode(adminPassword)) // 비밀번호 암호화하여 저장
                .nickname(nickname)
                .role(adminRole)
                .build();
            usersJpaRepository.save(admin);
           

    }
}
}