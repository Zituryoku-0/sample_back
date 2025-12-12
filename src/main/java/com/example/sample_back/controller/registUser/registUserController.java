package com.example.sample_back.controller.registUser;

import com.example.sample_back.service.login.UserEntity;
import com.example.sample_back.service.registUser.registUserService;
import com.example.sampleback.controller.RegistUserApi;
import com.example.sampleback.model.RequestRegistUser;
import com.example.sampleback.model.SuucessRegistUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class registUserController implements RegistUserApi {

    private final registUserService service;

    /**
     * POST /registUser
     *
     * @param requestRegistUser (optional)
     * @return Successfully (status code 200)
     * or Client Error (status code 400)
     * or Internal Server Error (status code 500)
     */
    @Override
    public ResponseEntity<SuucessRegistUser> registUserPost(RequestRegistUser requestRegistUser) {
        try {
            UserEntity entity = service.regist(requestRegistUser);
            SuucessRegistUser suucessRegistUser = new SuucessRegistUser();
            suucessRegistUser.setUserId(entity.getUserId());
            suucessRegistUser.setUserName(entity.getUserName());
            return ResponseEntity.ok(suucessRegistUser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
