package com.daitem.user_service.repository;

import com.daitem.user_service.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    boolean existsByRefreshToken(String refreshToken);

    void deleteByRefreshToken(String refreshToken);

    void deleteByUsername(String username);

    void deleteByCreatedAtBefore(LocalDateTime createdDate);
}
