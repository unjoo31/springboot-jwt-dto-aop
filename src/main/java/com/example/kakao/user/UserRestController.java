package com.example.kakao.user;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.kakao._core.utils.ApiUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserRestController {

    private final UserService userService;
    private final HttpSession session;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<?> join(@Valid UserRequest.JoinDTO requestDTO, BindingResult bindingResult) {
        userService.join(requestDTO);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserRequest.LoginDTO requestDTO, BindingResult bindingResult) {
        String jwt = userService.login(requestDTO);
        // 헤더에 토큰을 돌려준다
        return ResponseEntity.ok().header("Authorization", "Bearer "+jwt).body(ApiUtils.success(null));
    }

    // 로그아웃
    @GetMapping("/logout")
    public ResponseEntity<?> logout(){
        session.invalidate();
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
