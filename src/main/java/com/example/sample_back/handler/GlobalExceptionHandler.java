package com.example.sample_back.handler;

import com.example.sampleback.model.FailureLogin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<FailureLogin> handleIllegalArgumentException(IllegalArgumentException ex) {
        FailureLogin failureLogin = new FailureLogin();
        failureLogin.setUserId("");
        failureLogin.setUserName("");
        failureLogin.setLoginCheck(false);
        failureLogin.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(failureLogin);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<FailureLogin> handleGeneralException(Exception ex) {
        FailureLogin failureLogin = new FailureLogin();
        failureLogin.setUserId("");
        failureLogin.setUserName("");
        failureLogin.setLoginCheck(false);
        failureLogin.setMessage("内部サーバーエラーが発生しました。");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(failureLogin);
    }
}
