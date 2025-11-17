package com.daitem.user_service.service.impl;

import com.daitem.user_service.entity.User;
import com.daitem.user_service.entity.dto.CustomUserDetail;
import com.daitem.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


//로그인

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);

        if(user != null){
            return new CustomUserDetail(user);
        }else{
            throw new UsernameNotFoundException(email);
        }
    }
}
