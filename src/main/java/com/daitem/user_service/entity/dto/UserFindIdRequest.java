package com.daitem.user_service.entity.dto;


import jakarta.validation.constraints.Email;

public record UserFindIdRequest (@Email String email){
}
