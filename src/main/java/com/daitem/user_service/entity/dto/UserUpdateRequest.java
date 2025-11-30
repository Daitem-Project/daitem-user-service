package com.daitem.user_service.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest (
        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.") @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하입니다.")
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.") String password,
        @Size(min = 2, max = 10, message = "닉네임은 2~10자 사이여야 합니다.") String nickname,
        String profileUrl
){
}
