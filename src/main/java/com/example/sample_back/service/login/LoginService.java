package com.example.sample_back.service.login;

import com.example.sample_back.repository.login.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository repository;

    public UserEntity find() {
        var record = repository.select();
        return new UserEntity(record.getUserId(), record.getUserName());
    }
}
