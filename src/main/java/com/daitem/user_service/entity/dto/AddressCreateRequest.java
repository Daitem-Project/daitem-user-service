package com.daitem.user_service.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AddressCreateRequest (@NotBlank(message = "주소명칭을 입력해주세요.") String name,
                                    @NotBlank @Pattern(regexp = "^[0-9]{5,6}$", message = "우편번호는 5~6자리의 숫자만 가능합니다.")String addressNumber,
                                    @NotBlank String address,
                                    @NotBlank String addressDetail){}
