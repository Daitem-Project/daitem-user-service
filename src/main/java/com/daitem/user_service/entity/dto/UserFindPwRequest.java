package com.daitem.user_service.entity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserFindPwRequest(@NotBlank(message = "아이디는 필수 입력 항목입니다.")String username,
                                @Email String email,
                                String tempPassword) {
}
