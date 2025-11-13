package com.example.sample_back.controller.user;

import lombok.Value;

@Value
public class LoginRequest {
    String userId;
    String password;
}
