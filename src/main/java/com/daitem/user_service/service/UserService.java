package com.daitem.user_service.service;


import com.daitem.user_service.entity.SocialType;
import com.daitem.user_service.entity.User;
import com.daitem.user_service.entity.UserRole;
import com.daitem.user_service.entity.dto.*;
import com.daitem.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService extends DefaultOAuth2UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    /**
     * 일반유저 생성
     */
    public User createUser(UserCreatRequest request) {

        //중복검사
        if(userRepository.existsByUsername(request.username())){
            throw new IllegalArgumentException("이미 가입된 아이디 입니다.");
        }

        String encryptionPassword = passwordEncoder.encode(request.password());

        User user = User.createUser(request.username(), request.email(), encryptionPassword, request.name(), request.nickname(), request.phoneNumber(), request.profileUrl());


        userRepository.save(user);

        return user;
    }

    /**
     * 일반유저 수정
     */
    public Long updateUser(String username, UserUpdateRequest request) throws AccessDeniedException {
        String sessionUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!sessionUserName.equals(username)){
            throw new AccessDeniedException("본인 계정만 수정 가능합니다.");
        }

        User user = userRepository.findByUsernameAndIsLockedAndIsSocial(username, false, false).orElseThrow(()-> new UsernameNotFoundException(username));

        user.updateUser(request);


        return userRepository.save(user).getId();
    }

    /**
     * 소셜유저 수정
     * */
    public Long updateSocial(String username, SocialUpdateRequest request){

        User user = userRepository.findByUsernameAndIsLockedAndIsSocial(username, false, true).orElseThrow(()-> new UsernameNotFoundException(username));

        if(request.getEmail() != null){
            user.setEmail(request.getEmail());
        }
        if(request.getNickname() != null){
            user.setNickname(request.getNickname());
        }
        if(request.getProfileUrl() != null){
            user.setProfileUrl(request.getProfileUrl());
        }
        if(request.getPhoneNumber() != null){
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if(request.getName() != null){
            user.setName(request.getName());
        }

        return userRepository.save(user).getId();
    }


    /**
     * 일반회원로그인
     */

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameAndIsLockedAndIsSocial(username, false, false)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .accountLocked(user.isLocked())
                .build();
    }


    /**
     * 유저조회
     */
    @Transactional(readOnly = true)
    public UserResponse readUser(String username) {

        User user = userRepository.findByUsernameAndIsLocked(username, false)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다: " + username));

        return new UserResponse(username, user.isSocial(), user.getNickname(), user.getEmail(), user.getProfileUrl(), user.getPhoneNumber());
    }

    public List<User> getUsers() {
        return List.of();
    }


    /**
     * oauth 2 소셜로그인
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(request);

        Map<String, Object> attributes;
        List<GrantedAuthority> authorities;

        String username;
        String role = UserRole.USER.name();
        String email = null;
        String nickname = null;
        String name = null;
        String profileUrl = null;
        String phoneNumber = null;

        String registrationId = request.getClientRegistration().getRegistrationId().toUpperCase();
        if(registrationId.equals(SocialType.NAVER.name())){
            attributes = (Map<String, Object>)oauth2User.getAttributes().get("response");
            username = registrationId + "_" + attributes.get("id");
            email = attributes.get("email").toString();
            nickname = attributes.get("nickname").toString();
            name = attributes.get("name").toString();

            profileUrl = attributes.get("profile_image").toString();
            phoneNumber = attributes.get("mobile").toString();

        }else if(registrationId.equals(SocialType.GOOGLE.name())){
            attributes = (Map<String, Object>)oauth2User.getAttributes();
            username = registrationId + "_" + attributes.get("sub");
            email = attributes.get("email").toString();
            nickname = attributes.get("name").toString();
        }else if(registrationId.equals(SocialType.KAKAO.name())){
            attributes = (Map<String, Object>)oauth2User.getAttributes();

            Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");

            Map<String, Object> profile = (Map<String, Object>)kakaoAccount.get("profile");

            username = registrationId + "_" + attributes.get("id").toString();
            nickname = profile.get("nickname").toString();
            profileUrl = profile.get("profile_image_url").toString();
            email = kakaoAccount.get("email").toString();

        }else{
            throw new OAuth2AuthenticationException("지원하지 않는 소셜 로그인입니다.");
        }

        Optional<User> user = userRepository.findByUsernameAndIsSocial(username, true);

        if(user.isPresent()){
            role = user.get().getRole().name();

            SocialUpdateRequest dto = new  SocialUpdateRequest();

            dto.setEmail(email);
            dto.setNickname(nickname);
            dto.setName(name);
            dto.setProfileUrl(profileUrl);
            dto.setPhoneNumber(phoneNumber);

            updateSocial(user.get().getUsername(), dto);

            userRepository.save(user.get());

        }else{
            User newUser = User.createSocialUser(username, email, name, nickname, phoneNumber, profileUrl, SocialType.valueOf(registrationId));

            userRepository.save(newUser);


        }

        authorities = List.of(new SimpleGrantedAuthority(role));

        return new CustomOAuth2User(attributes, authorities, username);


    }

    /**
     * 회원탈퇴
     */
    public void deleteUser(String userName) throws AccessDeniedException {
        // 본인또는 관리자만 삭제가능
        SecurityContext context = SecurityContextHolder.getContext();
        String sessionUsername = context.getAuthentication().getName();
        String sessionRole = context.getAuthentication().getAuthorities().iterator().next().getAuthority();

        boolean isOwner = sessionUsername.equals(userName);
        boolean isAdmin = sessionRole.equals("ROLE_"+UserRole.ADMIN.name());

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("본인 계정만 탈퇴 할 수 있습니다.");
        }


        userRepository.deleteByUsername(userName);

        //탈퇴와 함께 토큰 제거
        jwtService.deleteByUserName(userName);
    }

    /**
     * 유저 존재여부 확인
     */
    @Transactional(readOnly = true)
    public boolean existsUser(String userName){
        return userRepository.existsByUsername(userName);
    }



}
