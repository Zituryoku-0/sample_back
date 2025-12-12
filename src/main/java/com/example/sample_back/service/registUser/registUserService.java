package com.example.sample_back.service.registUser;

import com.example.sample_back.repository.registUser.RegistUserRepository;
import com.example.sample_back.service.login.UserEntity;
import com.example.sampleback.model.RequestRegistUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class registUserService {

    private final RegistUserRepository registUserRepository;

    public UserEntity regist(RequestRegistUser requestRegistUser) {
        try {
            String userId;
            String userName;
            int result = registUserRepository.registUser(requestRegistUser.getUserId(), requestRegistUser.getUserName(), requestRegistUser.getPassword());
            if (result != 1) {
                throw new IllegalArgumentException("ユーザー登録に失敗しました。");
            }
            userId = requestRegistUser.getUserId();
            userName = requestRegistUser.getUserName();
            boolean loginCheck = true;
            return new UserEntity(userId, userName, loginCheck);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
