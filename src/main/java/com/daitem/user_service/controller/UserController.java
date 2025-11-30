package com.daitem.user_service.controller;

import com.daitem.user_service.entity.User;
import com.daitem.user_service.entity.dto.*;
import com.daitem.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid UserCreatRequest request){

        userService.createUser(request);

        return ResponseEntity.ok().build();
    }

    /**
     * 일반회원수정
     */
    @PutMapping("/user/user-update")
    public ResponseEntity<UserResponse> userUpdate(@AuthenticationPrincipal String username, @RequestBody @Valid UserUpdateRequest request) throws AccessDeniedException {

        userService.updateUser(username, request);

        return ResponseEntity.ok().build();
    }

    /**
     * 소셜회원수정
     */
    @PutMapping("/user/social-update")
    public ResponseEntity<UserResponse> socialUpdate(@AuthenticationPrincipal String username, @RequestBody @Valid SocialUpdateRequest request){

        userService.updateSocial(username, request);

        return ResponseEntity.ok().build();
    }


    /**
     * 회원조회
     */
    @GetMapping("/user/mypage")
    public ResponseEntity<UserResponse> readUser(@AuthenticationPrincipal String username){

        UserResponse response = userService.readUser(username);

        return ResponseEntity.ok().body(response);
    }

    /**
     * 회원탈퇴
     */
    @DeleteMapping("/user/delete")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal String username) throws AccessDeniedException {

        userService.deleteUser(username);

        return ResponseEntity.ok().build();
    }

    /**
     * 유저 존재여부
     */
    @PostMapping("/user/exist")
    public ResponseEntity<Boolean> existUser(@RequestBody Map<String, String> request){

        String userName = request.get("username");

        return ResponseEntity.ok(userService.existsUser(userName));
    }




}
