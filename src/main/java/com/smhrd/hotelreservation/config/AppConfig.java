package com.smhrd.hotelreservation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.siot.IamportRestClient.IamportClient;

@Configuration
public class AppConfig {
	
	String apiKey = "2862413137852468";
    String secretKey = "MsFW3zIa9ZjyzOFk9Rwt8xBwPjqvsGuswUpYUG6Qs6EqCiHXdaBdxMnmzXI1SmgBhJNdK0DAiGkcU3bQ";

    @Bean
    public IamportClient iamportClient() {
        return new IamportClient(apiKey, secretKey);
    }
}
