package com.example.sample_back.handler;

import com.example.sampleback.model.FailureLogin;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FailureLogin> handleValidationException(MethodArgumentNotValidException ex) {
        FailureLogin failureLogin = new FailureLogin();
        failureLogin.setUserId("");
        failureLogin.setUserName("");
        failureLogin.setLoginCheck(false);
        failureLogin.setMessage("入力値が不正です。");
        log.error("Invalid argument error", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failureLogin);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<FailureLogin> handleIllegalArgumentException(IllegalArgumentException ex) {
        FailureLogin failureLogin = new FailureLogin();
        failureLogin.setUserId("");
        failureLogin.setUserName("");
        failureLogin.setLoginCheck(false);
        failureLogin.setMessage("複数のユーザーが該当しました。");
        log.error("Invalid argument error", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failureLogin);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<FailureLogin> handleGeneralException(Exception ex) {
        FailureLogin failureLogin = new FailureLogin();
        failureLogin.setUserId("");
        failureLogin.setUserName("");
        failureLogin.setLoginCheck(false);
        failureLogin.setMessage("サーバー内部でエラーが発生しました。");
        log.error("Internal server error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(failureLogin);
    }
}
