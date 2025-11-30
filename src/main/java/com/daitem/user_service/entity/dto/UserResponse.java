package com.daitem.user_service.entity.dto;

public record UserResponse(String username,
                           boolean isSocial,
                           String nickname,
                           String email,
                           String profileUrl,
                           String phoneNumber) {
}
