package com.example.sample_back.controller.user;

import com.example.sample_back.service.login.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService service;

    @GetMapping
    public UserDTO checkUser(){
        var entity = service.find();
        return new UserDTO(entity.getUserId(), entity.getUserName() );
    }
}
