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
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        ErrorResponseInfo errorInfo = new ErrorResponseInfo();
        errorInfo.setCode("400");
        errorInfo.setMessage("fail");
        
        ErrorData errorData = new ErrorData();
        errorData.setUserId("");
        errorData.setUserName("");
        errorData.setLoginCheck(false);
        
        // バリデーションエラーメッセージを構築
        StringBuilder messageBuilder = new StringBuilder("入力値が不正です。");
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            messageBuilder.append(" ").append(error.getField()).append(": ").append(error.getDefaultMessage()).append("。");
        });
        errorData.setMessage(messageBuilder.toString());
        
        errorResponse.setResponseInfo(errorInfo);
        errorResponse.setData(errorData);
        log.error("Validation error", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalException(IllegalArgumentException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        ErrorResponseInfo errorInfo = new ErrorResponseInfo();
        errorInfo.setCode("400");
        errorInfo.setMessage("fail");
        ErrorData errorData = new ErrorData();
        errorData.setUserId("");
        errorData.setUserName("");
        errorData.setLoginCheck(false);
        errorData.setMessage(ex.getMessage());
        errorResponse.setResponseInfo(errorInfo);
        errorResponse.setData(errorData);
        log.error("Illegal argument error", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(FailureRegistUserException.class)
    public ResponseEntity<ErrorResponse> handleFailureRegistUserException(FailureRegistUserException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        ErrorResponseInfo errorInfo = new ErrorResponseInfo();
        errorInfo.setCode("500");
        errorInfo.setMessage("fail");
        ErrorData errorData = new ErrorData();
        errorData.setUserId("");
        errorData.setUserName("");
        errorData.setLoginCheck(false);
        errorData.setMessage(ex.getMessage());
        errorResponse.setResponseInfo(errorInfo);
        errorResponse.setData(errorData);
        log.error("failure regist user error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        ErrorResponseInfo errorInfo = new ErrorResponseInfo();
        errorInfo.setCode("500");
        errorInfo.setMessage("fail");
        ErrorData errorData = new ErrorData();
        errorData.setUserId("");
        errorData.setUserName("");
        errorData.setLoginCheck(false);
        errorData.setMessage(ex.getMessage());
        errorResponse.setResponseInfo(errorInfo);
        errorResponse.setData(errorData);
        log.error("Internal server error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
