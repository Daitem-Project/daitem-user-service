package com.daitem.user_service.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailHelper {

    private final EmailService emailService;

}
