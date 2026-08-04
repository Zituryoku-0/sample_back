package com.example.sample_back.controller.registUser;

import com.example.sample.generated.api.RegistUserApi;
import com.example.sample.generated.model.RequestRegistUser;
import com.example.sample.generated.model.SuccessRegistUser;
import com.example.sample_back.service.registUser.RegistUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegistUserController implements RegistUserApi {

    private final RegistUserService service;

    /**
     * POST /registUser : ユーザー登録
     * ユーザー登録
     *
     * @param requestRegistUser (optional)
     * @return Successfully (status code 200)
     * or Client Error (status code 400)
     * or Unauthorized (status code 401)
     * or Internal Server Error (status code 500)
     */
    @Override
    public ResponseEntity<SuccessRegistUser> registUser(RequestRegistUser requestRegistUser) {
        SuccessRegistUser entity = service.regist(requestRegistUser);
        return ResponseEntity.ok(entity);
    }
}
