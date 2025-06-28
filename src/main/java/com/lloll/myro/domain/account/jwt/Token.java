package com.lloll.myro.domain.account.jwt;

import lombok.Getter;

@Getter
//@RequiredArgsConstructor
public class Token {
//    private final String token;
    private String token;
    public Token(String token) {
        this.token = token;
    }
}
