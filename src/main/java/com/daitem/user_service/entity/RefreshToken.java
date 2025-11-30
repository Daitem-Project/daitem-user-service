package com.daitem.user_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Table(name = "refreshToken")
public class RefreshToken {

    @Id
    @Column(name = "refreshToken_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String username;

    @Column(name = "refreshToken", length = 512, nullable = false)
    private String refreshToken;

    @CreatedDate
    @Column(name = "refresh_created_at", updatable = false)
    private LocalDateTime createdAt;
}
