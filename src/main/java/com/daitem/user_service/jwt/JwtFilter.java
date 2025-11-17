package com.daitem.user_service.jwt;

import com.daitem.user_service.entity.User;
import com.daitem.user_service.entity.UserRole;
import com.daitem.user_service.entity.dto.CustomUserDetail;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;



    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if(authorization == null || !authorization.startsWith("Bearer ")) {

            //임시
            System.out.println("authorization is null");
            filterChain.doFilter(request,response);
            return;
        }

        String token = authorization.split(" ")[1];

        if(jwtUtil.isExpired(token)) {
            System.out.println("token is expired");
            filterChain.doFilter(request,response);

            return;
        }

        String email = jwtUtil.getUserEmail(token);
        String role = jwtUtil.getRole(token);

        User user = new User();
        user.setEmail(email);
        user.setPassword("temporarypassword");
        try{
            user.setRole(UserRole.valueOf(role));
        }catch (IllegalArgumentException | NullPointerException e){
            System.out.println("유효하지 않은 권한 " + role);
            user.setRole(UserRole.ROLE_USER);
        }

        CustomUserDetail customUserDetail = new CustomUserDetail(user);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetail, null, customUserDetail.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request,response);



    }

/**
 * 정상적인 요청 이외에 발생할 수 있는 예외 처리
 * **/
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.equals("/api/login") || path.equals("/") || path.equals("/api/signup");
    }

}
