package com.smhrd.hotelreservation.config;

import com.smhrd.hotelreservation.web.resolver.LoginUserArgumentResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/*
 * 첨부 파일의 경로를 맵핑하기 위한 설정 클래스
 * 
 * 이미지 활용 구현을 위해서 새로 작성됨
 * 
 * application.yaml에 file에 해당하는 webpath와 filepath가 새로 작성됨
 * */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	
	
	@Value("${file.filepath}")
    String filepath; // 첨부 폴더 경로

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 파일 경로와 상대경로 맵핑
        registry.addResourceHandler("/uploadfile/**")
        .addResourceLocations("file:" + filepath);


    }
    
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginUserArgumentResolver());
    }
}
