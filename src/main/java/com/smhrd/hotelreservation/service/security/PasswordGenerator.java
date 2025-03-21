package com.smhrd.hotelreservation.service.security;

import java.security.SecureRandom;

public class PasswordGenerator {
	  // 임시 비밀번호 길이
    private static final int PASSWORD_LENGTH = 10;
    
    // 임시 비밀번호 생성 메소드
    public static String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }
        return sb.toString();
    }
}
