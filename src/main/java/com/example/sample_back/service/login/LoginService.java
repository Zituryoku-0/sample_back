package com.example.sample_back.service.login;

import com.example.sample_back.controller.user.LoginRequest;
import com.example.sample_back.repository.login.UserRecord;
import com.example.sample_back.repository.login.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository repository;

    public UserEntity find(LoginRequest request) {
        try {
            boolean loginCheck = false;
            String userId = "";
            String userName = "";
            UserRecord resSelect = repository.select(request.getUserId(), request.getPassword());
            if (resSelect != null) {
                userId = resSelect.getUserId().trim();
                userName = resSelect.getUserName().trim();
                loginCheck = true;
            }
            return new UserEntity(userId, userName, loginCheck);
        } catch (TooManyResultsException tooManyResultsException) {
            // ユーザーIDが複数件ヒットした場合は、例外を返す
            log.error("Too many results returned for userId = {}", request.getUserId(), tooManyResultsException);
            throw new IllegalArgumentException("複数のユーザーが該当しました", tooManyResultsException);
        } catch (Exception e) {
            log.error("An exception has occurred", e);
            throw new RuntimeException("何かしらの例外が発生しました。", e);
        }
    }
}
