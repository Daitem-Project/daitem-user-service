package com.daitem.user_service.entity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SocialUpdateRequest {
    @Size(min = 2, max = 10, message = "닉네임은 2~10자 사이여야 합니다.")
    String nickname;
    @NotBlank
    String name;
    @Email
    String email;
    String profileUrl;
    @Pattern(regexp = "^[0-9]{11}$", message = "전화번호는 하이픈 제외 11자리 숫자여야 합니다.")
    String phoneNumber;
}

//name, email, nickname, profileUrl, phoneNumber
