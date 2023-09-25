package com.example.kakao.user;

import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;


public class UserRequest {
    @Getter
    @Setter
    @ToString
    public static class JoinDTO {

        @NonNull
        private String email;

        @NonNull
        @Size(min = 4, max = 10)
        private String password;

        @NonNull
        private String username;

        public User toEntity() {
            return User.builder()
                    .email(email)
                    .password(password)
                    .username(username)
                    .build();
        }
    }

    @Getter
    @Setter
    @ToString
    public static class LoginDTO {
        @NonNull
        @Size(min = 4, max = 10)
        private String email;

        @NonNull
        @Size(min = 4, max = 10)
        private String password;
    }
}
