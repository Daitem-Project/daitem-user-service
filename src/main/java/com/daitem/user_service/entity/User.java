package com.daitem.user_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
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
    @Column(name = "user_password")
    private String password;

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

    @Column(name = "social_id")
    private String socialId;

    @Column(name = "social_name")
    private String socialName;

    @PrePersist
    protected void onPrePersist(){

    }

    /**
     * 회원가입
     **/


    @Builder(builderMethodName = "standard")
    public User(String email, String password, String name, String nickname, String phoneNumber, String profileUrl) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.profileUrl = profileUrl;

        //기본값
        this.createdAt = LocalDateTime.now();
        this.rating = 0.0;
        this.point = 0;
    }

//    @Builder(builderMethodName = "social")
//    public User(String socialId, String socialName) {
//
//    }
}
