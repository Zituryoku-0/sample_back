package com.example.sample_back.controller.user;

import com.example.sample_back.service.login.LoginService;
import com.example.sample_back.service.login.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService service;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> checkUser(@RequestBody LoginRequest request) {
        try {
            UserEntity entity = service.find(request);
            return ResponseEntity.ok(entity);
        } catch (IllegalArgumentException illegalArgumentException) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(illegalArgumentException.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}
