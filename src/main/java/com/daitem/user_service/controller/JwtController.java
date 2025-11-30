package com.daitem.user_service.controller;

import com.daitem.user_service.entity.dto.JwtResponse;
import com.daitem.user_service.entity.dto.RefreshRequest;
import com.daitem.user_service.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jwt")
public class JwtController {

    private final JwtService jwtService;

    // 소셜 로그인 쿠키 방식의 Refresh 토큰 헤더 방식으로 교환
    @PostMapping("/exchange")
    public JwtResponse exchangeToken(HttpServletRequest request, HttpServletResponse response) {

        return jwtService.cookie2Header(request, response);
    }

    // Refresh 토큰으로 Access 토큰 재발급 (Rotate 포함)
    @PostMapping("/refresh")
    public JwtResponse jwtRefresh(@RequestBody @Valid RefreshRequest request) {

        return jwtService.refreshRotate(request);
    }
}
