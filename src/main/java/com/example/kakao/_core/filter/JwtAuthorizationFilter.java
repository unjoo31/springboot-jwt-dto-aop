package com.example.kakao._core.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.kakao._core.errors.exception.Exception401;
import com.example.kakao._core.utils.ApiUtils;
import com.example.kakao._core.utils.JwtTokenUtils;
import com.example.kakao._core.utils.ApiUtils.ApiResult;
import com.example.kakao.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * /carts/**
 * /orders/**
 * /products/**
 * 이 주소만 필터가 동작하면 된다
 */
public class JwtAuthorizationFilter implements Filter{

    // 특정 주소에만 접근할 수 있는 filter
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        
        // 다운캐스팅
        HttpServletRequest request = (HttpServletRequest) req; 
        HttpServletResponse response = (HttpServletResponse) resp;
        
        // 토큰 꺼내기
        // header의 "Authorization"을 찾는다 -> 브라우저는 cookie에 저장하지 않는다. Authorization은 직접 키값을 꺼내서 저장해야한다 → 인가가 필요한 상황에 꺼내서 사용하기 위함이다.
        String jwt = request.getHeader("Authorization");

        // 토큰이 없거나 비어있는 경우 걸러내기
        if(jwt == null || jwt.isEmpty()){
            onError(response, "토큰 없음");
            return; // 아래 코드를 실행하지 않고 돌아가게 만들기
        }

        // 검증하기
        try {
            DecodedJWT decodedJWT = JwtTokenUtils.verify(jwt);

            // 파싱된 id, email값을 꺼내기
            int userId = decodedJWT.getClaim("id").asInt();
            String email = decodedJWT.getClaim("email").asString();

            // session을 저장공간으로 직접 만들기 : controller에서 꺼내쓰기 쉽게하려고
            User sessionUser = User.builder().id(userId).email(email).build();
            HttpSession session = request.getSession();
            session.setAttribute("sessionUser", sessionUser);

            // 다음 filter로 이동해라
            chain.doFilter(request, response);
        } catch (SignatureVerificationException | JWTDecodeException e1) {  // Exception의 경우 3개임
            // 토큰 검증실패
            onError(response, "토큰 검증 실패");
        } catch (TokenExpiredException e2){
            // 토큰 만료
            onError(response, "토큰 시간 만료");
        }
        
    }

    // filter는 디스패쳐서블릿 전에 작동하기 때문에 throw를 던질 수 없가 때문에 직접 만들어야 함.
    // ExceptionHandler를 호출할 수 없다.
    private void onError(HttpServletResponse response, String msg) {
        Exception401 e401 = new Exception401("인증되지 않았습니다");

        try {
            // messageconvertor의 도움을 받을 수 없기 때문에 객체를 직접 생성해줘야함 
            String body = new ObjectMapper().writeValueAsString(e401.body());
            response.setStatus(e401.status().value());
            // body 데이터를 넣어줄 때 mine-type을 직접 만들어서 넘겨줘야한다 (원래는 messageconvertor가 해준다)
            // response.setHeader("Content-Type", "application/json; charset=utf-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            // 응답의 버퍼에 직접 접근해서 담는다.
            PrintWriter out = response.getWriter(); // PrintWriter : autoflush가 적용되어 있다.
            out.println(body);

            // 상대방 애플리케이션이 버퍼에 담긴 데이터를 읽을 때 버퍼의 세팅값을 기준으로 읽는다. 
            // 버퍼에서 몇바이트씩 끊어서 읽을지 설정해야함 -> 브라우저가 아닌 앱의 경우 별도의 설정을 해줘야 한글이 끊어지지 않는다.(한글 : 3Byte)

        } catch (Exception e) {
            System.out.println("파싱 에러가 날 수 없음");
        }
    }
}
