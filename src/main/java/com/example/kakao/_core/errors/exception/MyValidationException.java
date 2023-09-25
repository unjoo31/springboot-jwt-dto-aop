package com.example.kakao._core.errors.exception;

import java.util.Map;

import lombok.Getter;

@Getter
public class MyValidationException extends RuntimeException{
    
    private Map<String, String> erroMap;

    public MyValidationException(Map<String, String> erroMap){
        this.erroMap = erroMap;
    }
}
