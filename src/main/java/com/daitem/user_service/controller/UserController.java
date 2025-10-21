package com.daitem.user_service.controller;

import com.daitem.user_service.entity.User;
import com.daitem.user_service.entity.dto.UserCreatRequest;
import com.daitem.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    


    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid UserCreatRequest request){

        User user = userService.createUser(request);

        return ResponseEntity.ok().build();
    }
}
