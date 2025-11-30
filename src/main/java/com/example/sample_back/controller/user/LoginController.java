package com.example.sample_back.controller.user;

import com.example.sample_back.service.login.LoginService;
import com.example.sample_back.service.login.UserEntity;
import com.example.sampleback.controller.LoginApi;
import com.example.sampleback.model.LoginGetRequest;
import com.example.sampleback.model.LoginPostRequest;
import com.example.sampleback.model.RequestLogin;
import com.example.sampleback.model.SuccessLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<SuccessLogin> loginPost(RequestLogin requestLogin) {
        try {
            UserEntity entity = service.find(requestLogin);
            SuccessLogin successLogin = new SuccessLogin();
            successLogin.setUserId(entity.getUserId());
            successLogin.setUserName(entity.getUserName());
            successLogin.setLoginCheck(entity.getLoginCheck());
            return ResponseEntity.ok(successLogin);
        } catch (IllegalArgumentException illegalArgumentException) {
<<<<<<< Updated upstream
           throw illegalArgumentException;
=======
            throw illegalArgumentException;
>>>>>>> Stashed changes
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
