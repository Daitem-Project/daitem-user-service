package com.daitem.user_service.service.impl;


import com.daitem.user_service.entity.User;
import com.daitem.user_service.entity.dto.UserCreatRequest;
import com.daitem.user_service.entity.dto.UserUpdateRequest;
import com.daitem.user_service.repository.UserRepository;
import com.daitem.user_service.service.UserService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 생성
     */
    @Override
    public User createUser(UserCreatRequest request) {

        //중복검사
        if(userRepository.existsByEmail(request.email())){
            throw new IllegalArgumentException("이미 가입된 이메일 입니다.");
        }

        String encryptionPassword = passwordEncoder.encode(request.password());

        User user = User.standard().
                email(request.email()).
                password(encryptionPassword).
                name(request.name()).
                nickname(request.nickname()).
                phoneNumber(request.phoneNumber()).
                profileUrl(request.profileUrl()).
                build();


        userRepository.save(user);

        return user;
    }

    @Override
    public User updateUser(UserUpdateRequest request) {
        return null;
    }

    @Override
    public User getUser(long id) {
        return null;
    }

    @Override
    public List<User> getUsers() {
        return List.of();
    }

    @Override
    public void deleteUser(long id) {

    }
}
