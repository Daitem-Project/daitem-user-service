package com.daitem.user_service.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//security config에서 설정한방식과 동일하지만 필터를 거치지않고 controller까지 넘어가는경우 적용
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**").allowedOrigins("http://localhost:3000");
    }
}
