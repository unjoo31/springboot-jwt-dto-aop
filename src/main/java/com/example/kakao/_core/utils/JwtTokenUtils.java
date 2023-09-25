package com.example.kakao._core.utils;

import java.time.Instant;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.kakao.user.User;

public class JwtTokenUtils {

    // token 생성
    public static String create(User user) {
        String jwt = JWT.create()
                        .withSubject("metacoding-key")
                        .withClaim("id", user.getId()) // PAYLOAD에 들어간다
                        .withClaim("email", user.getEmail()) // PAYLOAD에 들어간다
                        .withExpiresAt(Instant.now().plusMillis(1000*60*60*24*7L)) // 토큰 만료시간 : 현재시간 + 1초
                        .sign(Algorithm.HMAC512("meta")); // HEADER + PAYLOAD의 값을 HMAC512로 암호화한다
                        // .sign(Algorithm.HMAC512("meta")) : meta가 대칭키의 이름이다. 원래는 환경변수에 저장해두는 것이 맞다
        return "Bearer " + jwt;
    }

    // token 검증
    public static DecodedJWT verify(String jwt)
            throws SignatureVerificationException, TokenExpiredException {

        // Bearer 인증방식 : jwt를 사용한 인증방식을 의미함
        jwt = jwt.replace("Bearer ", ""); // client한테 받아서 디코딩할때는 공백을 없앤다
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512("meta"))
                .build().verify(jwt);
        // decodedJWT 객체가 만들어지면 검증된 것. 만들어지지 않으면 검증되지 않은 것.
        // verify 하고나서 HEADER + PAYLOAD가 디코드 된 값이 decodedJWT 객체로 생성됨
        // JWT 라이브러리에서 디코딩 된 값을 알아서 파싱해서 줌
        return decodedJWT;
    }
}
