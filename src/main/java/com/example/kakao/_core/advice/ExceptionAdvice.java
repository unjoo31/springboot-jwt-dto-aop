package com.example.kakao._core.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.kakao._core.errors.exception.MyValidationException;

@RestControllerAdvice
public class ExceptionAdvice {
    
    @ExceptionHandler(MyValidationException.class)
    public ResponseEntity<?> error(MyValidationException e){
        return new ResponseEntity<>(e.getErroMap().toString(), HttpStatus.BAD_REQUEST);
    }
}
