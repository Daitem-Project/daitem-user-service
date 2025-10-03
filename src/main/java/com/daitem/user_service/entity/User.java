package com.daitem.user_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotNull
    @Column(name = "user_email")
    private String email;

    @NotNull
    @Column(name = "user_name")
    private String name;

    @NotNull
    @Column(name = "user_nickname")
    private String nickname;

    @NotNull
    @Column(name = "user_number")
    private String phoneNumber;

    @Column(name = "user_profile_url")
    private String profileUrl;

    @NotNull
    @Column(name = "user_rating")
    private double rating;

    @NotNull
    @Column(name = "user_point")
    private int point;

    @NotNull
    @Column(name = "user_created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onPrePersist(){
        this.createdAt = LocalDateTime.now();
        this.rating = 0.0;
        this.point = 0;
    }
}
