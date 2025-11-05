package com.example.sample_back.repository.login;

import com.example.sample_back.service.login.UserEntity;
import lombok.Value;

@Value
public class UserRecord {
    String userId;
    String userName;
    String userPassword;
}
