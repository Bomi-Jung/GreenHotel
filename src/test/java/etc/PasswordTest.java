package etc;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordTest {

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    public void 암호화테스트() {
        String password = "1234";

        // 비밀번호 암호화
        String enPw = passwordEncoder.encode(password);

        System.out.println("enPw: " + enPw);

        // 비밀번호가 1234가 맞는지 확인 (문자열과 해시코드 비교)
        boolean matchResult = passwordEncoder.matches(password, enPw);

        System.out.println("확인결과: " + matchResult);
    }
}