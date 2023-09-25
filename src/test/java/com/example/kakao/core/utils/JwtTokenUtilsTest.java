package com.example.kakao.core.utils;

import org.junit.jupiter.api.Test;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.kakao._core.utils.JwtTokenUtils;
import com.example.kakao.user.User;

public class JwtTokenUtilsTest {
    
    // token 생성
    @Test
    public void jwt_create_test(){
        User user = User.builder().id(1).email("ssar@nate.com").build();
        String jwt = JwtTokenUtils.create(user);
        System.out.println(jwt);
    }

    // token 검증
    @Test
    public void jwt_verify_test(){
        String jwt = "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJtZXRhY29kaW5nLWtleSIsImlkIjoxLCJlbWFpbCI6InNzYXJAbmF0ZS5jb20iLCJleHAiOjE2OTYyMjQ2MzN9.Gi3hZcFD9YUpXAD2-Ew63PkOg-1Ym2OGkAZCpdHIZSIxVah3Kb_gs-HkKVBGA5f1PwoGyUfFEXaJ2q6kpMOrLg";
        DecodedJWT decodedJWT = JwtTokenUtils.verify(jwt);

        // 검증이 끝나면 꺼내쓸 수 있다
        int id = decodedJWT.getClaim("id").asInt();
    }
}
