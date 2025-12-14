package com.example.sample_back.handler;

import com.example.sample_back.exception.FailureRegistUserException;
import com.example.sampleback.model.*;
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

    @ExceptionHandler(FailureRegistUserException.class)
    public ResponseEntity<ErrorResponse> handleFailureRegistUserException(FailureRegistUserException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        ErrorResponseInfo erorrInfo = new ErrorResponseInfo();
        erorrInfo.setCode("500");
        erorrInfo.setMessage("fail");
        ErrorData errorData = new ErrorData();
        errorData.setUserId("");
        errorData.setUserName("");
        errorData.setLoginCheck(false);
        errorData.setMessage(ex.getMessage());
        errorResponse.setResponseInfo(erorrInfo);
        errorResponse.setData(errorData);
        log.error("failure regist user error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleFailureRegistUserGeneralException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        ErrorResponseInfo erorrInfo = new ErrorResponseInfo();
        erorrInfo.setCode("500");
        erorrInfo.setMessage("fail");
        ErrorData errorData = new ErrorData();
        errorData.setUserId("");
        errorData.setUserName("");
        errorData.setLoginCheck(false);
        errorData.setMessage(ex.getMessage());
        errorResponse.setResponseInfo(erorrInfo);
        errorResponse.setData(errorData);
        log.error("Internal server error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
