package com.daitem.user_service.config;

import com.daitem.user_service.entity.UserRole;
import com.daitem.user_service.handler.RefreshTokenLogoutHandler;
import com.daitem.user_service.jwt.JwtFilter;
import com.daitem.user_service.jwt.JwtUtil;
import com.daitem.user_service.jwt.LoginFilter;
import com.daitem.user_service.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final AuthenticationSuccessHandler loginSuccessHandler;
    private final AuthenticationSuccessHandler socialSuccessHandler;
    private final JwtService jwtService;
    private final JwtUtil jwtUtil;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration,
                          @Qualifier("LoginSuccessHandler")AuthenticationSuccessHandler loginSuccessHandler,
                          @Qualifier("SocialSuccessHandler")AuthenticationSuccessHandler socialSuccessHandler, JwtService jwtService, JwtUtil jwtUtil) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.loginSuccessHandler = loginSuccessHandler;
        this.socialSuccessHandler = socialSuccessHandler;

        this.jwtService = jwtService;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(jwtUtil);
    }

    @Bean
    public LoginFilter loginFilter(AuthenticationManager authenticationManager) throws Exception {
        return new LoginFilter(authenticationManager, loginSuccessHandler);
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("Authorization", "Set-Cookie"));
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // 권한 계층
    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withRolePrefix("ROLE_")
                .role(UserRole.ADMIN.name()).implies(UserRole.USER.name())
                .build();
    }




    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, LoginFilter loginFilter, JwtFilter jwtFilter) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        // CSRF 보안 필터 disable
        http
                .csrf(AbstractHttpConfigurer::disable);


        // 기본 Form 기반 인증 필터들 disable
        http
                .formLogin(AbstractHttpConfigurer::disable);

        // 기본 Basic 인증 필터 disable
        http
                .httpBasic(AbstractHttpConfigurer::disable);

        // 2. HTTP Header 설정: H2 Console의 iFrame을  임시 허용
        http
                .headers((headers) -> headers
                        // X-Frame-Options 헤더를 sameOrigin으로 설정하여 iFrame 허용
                        .frameOptions((frameOptions) -> frameOptions.sameOrigin())
                );

        //oauth2 인증용
        http
                .oauth2Login(oauth2->oauth2
                        .loginProcessingUrl("/api/login/oauth2/code/*")
                        .successHandler(socialSuccessHandler));



        http
                .authorizeHttpRequests((auth) -> auth
                                .requestMatchers(
                                        "/api/signup",
                                        "/api/login",
                                        "/api/user/exist",
                                        "/api/jwt/exchange",
                                        "/api/jwt/refresh",
                                        "/",
                                        "/h2-console/**"
                                ).permitAll()

                                .requestMatchers("/oauth2/authorization/**").permitAll()        // 시작
                                .requestMatchers("/api/login/oauth2/code/**").permitAll()
                                // 콜백

                                .requestMatchers("/api/user/**").hasRole("USER")

                                .requestMatchers("/admin").hasRole("ADMIN")

                                .anyRequest().authenticated()
                );

        // 커스텀 필터 추가

        http
                .addFilterBefore(jwtFilter, LogoutFilter.class);
        http
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);



        // 기본 로그아웃 필터 + 커스텀 Refresh 토큰 삭제 핸들러 추가
        http
                .logout(logout -> logout
                        .addLogoutHandler(new RefreshTokenLogoutHandler(jwtService, jwtUtil)));

        // 예외 처리
        http
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED); // 401 응답
                        })
                        .accessDeniedHandler((request, response, authException) -> {
                            response.sendError(HttpServletResponse.SC_FORBIDDEN); // 403 응답
                        })
                );

        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
