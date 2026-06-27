package com.daitem.user_service.service;

import com.daitem.user_service.entity.User;
import com.daitem.user_service.entity.dto.UserFindIdRequest;
import com.daitem.user_service.entity.dto.UserFindPwRequest;
import com.daitem.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmailService {
    private final JavaMailSender mailSender;

    private final UserRepository userRepository;


    /**
     * 유저아이디 찾기
     */
    @Async
    public void sendUserName(UserFindIdRequest request){
        User user = userRepository.findByEmail(request.email()).orElseThrow(()-> new IllegalArgumentException("가입내역이 존재하지 않습니다."));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.email());
        message.setSubject("[Daitem] 아이디 찾기 결과");
        message.setText("회원님의 아이디는 [ "+user.getUsername()+" ] 입니다.");

        try{
            mailSender.send(message);
        }catch (Exception e){
            log.error("메일 발송 실패 {}", e.getMessage());
            throw new RuntimeException("메일 발송 실패"+e.getMessage());
        }

    }

    /**
     * 유저 비밀번호찾기
     */
    @Async
    public void sendUserPassword(UserFindPwRequest request){

        User user = userRepository.findByUsername(request.username()).orElseThrow(()-> new UsernameNotFoundException("유저를 찾을 수 없습니다."));

        if(!user.getEmail().equals(request.email())){
            throw new IllegalArgumentException("이메일 정보가 같지 않습니다.");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.email());
        message.setSubject("[Daitem] 임시 비밀번호 발급");
        message.setText(user.getUsername() + "님의 임시 비밀번호는 " + request.tempPassword() + " 입니다.");

        try{
            mailSender.send(message);
        }catch (Exception e){
            log.error("메일 발송 실패 {}", e.getMessage());
            throw new RuntimeException("메일 발송 실패"+e.getMessage());
        }

    }

}
