package com.example.sample_back.controller.registUser;

import com.example.sample_back.exception.FailureRegistUserException;
import com.example.sample_back.service.registUser.RegistUserService;
import com.example.sampleback.controller.RegistUserApi;
import com.example.sampleback.model.RequestRegistUser;
import com.example.sampleback.model.ResponseSuccessRegistUser;
import com.example.sampleback.model.SuccessRegistUser;
import com.example.sampleback.model.SuccessResponseInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegistUserController implements RegistUserApi {

    private final RegistUserService service;

    /**
     * POST /registUser
     *
     * @param requestRegistUser (optional)
     * @return Successfully (status code 200)
     * or Client Error (status code 400)
     * or Internal Server Error (status code 500)
     */
    @Override
    public ResponseEntity<SuccessRegistUser> registUserPost(RequestRegistUser requestRegistUser) {
        SuccessRegistUser entity = service.regist(requestRegistUser);
        return ResponseEntity.ok(entity);
    }
}
