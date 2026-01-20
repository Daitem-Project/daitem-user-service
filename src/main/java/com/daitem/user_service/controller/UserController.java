package com.daitem.user_service.controller;

import com.daitem.user_service.entity.User;
import com.daitem.user_service.entity.dto.*;
import com.daitem.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@ModelAttribute @Valid UserCreatRequest request,
                                       @RequestPart(value = "profileImage", required = false) MultipartFile profileImage){

        userService.createUser(request, profileImage);

        return ResponseEntity.ok().build();
    }

    /**
     * 일반회원수정
     */
    @PutMapping("/user-update")
    public ResponseEntity<UserResponse> userUpdate(@AuthenticationPrincipal String username, @RequestBody @Valid UserUpdateRequest request) throws AccessDeniedException {

        userService.updateUser(username, request);

        return ResponseEntity.ok().build();
    }

    /**
     * 소셜회원수정
     */
    @PutMapping("/social-update")
    public ResponseEntity<UserResponse> socialUpdate(@AuthenticationPrincipal String username, @RequestBody @Valid SocialUpdateRequest request){

        userService.updateSocial(username, request);

        return ResponseEntity.ok().build();
    }


    /**
     * 회원조회
     */
    @GetMapping("/mypage")
    public ResponseEntity<UserResponse> readUser(@AuthenticationPrincipal String username){

        UserResponse response = userService.readUser(username);

        return ResponseEntity.ok().body(response);
    }

    /**
     * 회원탈퇴
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal String username) throws AccessDeniedException {

        userService.deleteUser(username);

        return ResponseEntity.ok().build();
    }

    /**
     * 유저 존재여부
     */
    @PostMapping("/exist")
    public ResponseEntity<Boolean> existUser(@RequestBody Map<String, String> request){

        String userName = request.get("username");

        return ResponseEntity.ok(userService.existsUser(userName));
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUser(@RequestBody Map<String, String> refresh) {

        String refreshToken = refresh.get("refreshToken");

        userService.logoutUser(refreshToken);

        return ResponseEntity.ok().build();
    }

    /**
     * 프로필 이미지설정
     */
    @PostMapping("/profile-image")
    public ResponseEntity<?> updateProfile(@RequestParam("profileImage") MultipartFile profile,
                                           @AuthenticationPrincipal String username){
        // 1. 서비스 호출 -> 파일 저장 -> URL 반환 (/images/uuid.jpg)
        String imageUrl = userService.uploadProfile(profile);

        // 2. DB 업데이트 (Member 엔티티의 profileUrl 필드 수정)
        userService.updateProfileUrl(username, imageUrl);

        // 3. 프론트로 결과 반환
        Map<String, String> response = new HashMap<>();
        response.put("newProfileUrl", imageUrl); // 프론트는 이 URL을 바로 img src에 꽂으면 됨

        return ResponseEntity.ok(response);
    }

    /**
     * 프로필이미지 삭제
     */
    @DeleteMapping("/profile-image")
    public ResponseEntity<Void> deleteProfileImage(@AuthenticationPrincipal String username) {

        userService.deleteProfileUrl(username);

        return ResponseEntity.ok().build();
    }




}
