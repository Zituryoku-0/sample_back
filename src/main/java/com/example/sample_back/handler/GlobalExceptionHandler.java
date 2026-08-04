package com.example.sample_back.handler;

import com.example.sample.generated.model.ErrorData;
import com.example.sample.generated.model.ErrorResponse;
import com.example.sample.generated.model.ErrorResponseInfo;
import com.example.sample_back.exception.FailureRegistUserException;
import com.example.sample_back.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalException(IllegalArgumentException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        ErrorResponseInfo erorrInfo = new ErrorResponseInfo();
        erorrInfo.setCode("400");
        erorrInfo.setMessage("fail");
        ErrorData errorData = new ErrorData();
        errorData.setUserId("");
        errorData.setUserName("");
        errorData.setLoginCheck(false);
        errorData.setMessage(ex.getMessage());
        errorResponse.setResponseInfo(erorrInfo);
        errorResponse.setData(errorData);
        log.error("Illegal argument error", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        ErrorResponseInfo erorrInfo = new ErrorResponseInfo();
        erorrInfo.setCode("401");
        erorrInfo.setMessage("fail");
        ErrorData errorData = new ErrorData();
        errorData.setUserId("");
        errorData.setUserName("");
        errorData.setLoginCheck(false);
        errorData.setMessage(ex.getMessage());
        errorResponse.setResponseInfo(erorrInfo);
        errorResponse.setData(errorData);
        log.error("Illegal argument error", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
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
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
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
