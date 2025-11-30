package com.daitem.user_service.entity.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SocialUpdateRequest {
    @Size(min = 2, max = 10, message = "닉네임은 2~10자 사이여야 합니다.")
    String nickname;

    String name;

    String email;

    String profileUrl;

    String phoneNumber;
}

//name, email, nickname, profileUrl, phoneNumber
