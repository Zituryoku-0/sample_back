package com.example.sample_back.controller.user;

import com.example.sample_back.service.login.LoginService;
import com.example.sample_back.service.login.UserEntity;
import com.example.sampleback.controller.LoginApi;
import com.example.sampleback.model.RequestLogin;
import com.example.sampleback.model.SuccessLoginUser;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController implements LoginApi {

    private final LoginService service;

    /**
     * POST /login
     *
     * @param requestLogin (optional)
     * @return Successfully (status code 200)
     * or Client Error (status code 400)
     * or Internal Server Error (status code 500)
     */
    @Override
    public ResponseEntity<SuccessLoginUser> loginPost(RequestLogin requestLogin) {
        SuccessLoginUser response = service.find(requestLogin);
        return ResponseEntity.ok(response);
    }



}
