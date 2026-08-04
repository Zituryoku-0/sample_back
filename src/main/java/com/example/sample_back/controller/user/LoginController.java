package com.example.sample_back.controller.user;

import com.example.sample.generated.api.LoginApi;
import com.example.sample.generated.model.RequestLogin;
import com.example.sample.generated.model.SuccessLoginUser;
import com.example.sample_back.service.login.LoginService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController implements LoginApi {

    private final LoginService service;

    /**
     * POST /login : ユーザーログイン
     * ユーザーログイン
     *
     * @param requestLogin (optional)
     * @return Successfully (status code 200)
     * or Client Error (status code 400)
     * or Internal Server Error (status code 500)
     */
    @Override
    public ResponseEntity<SuccessLoginUser> loginUser(RequestLogin requestLogin) {
        SuccessLoginUser response = service.find(requestLogin);
        return ResponseEntity.ok(response);
    }
}
