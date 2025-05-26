package com.lloll.myro.domain.account.user.application.response;

import com.lloll.myro.domain.account.jwt.Token;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponse {

    private Token accessToken;

    public LoginResponse(Token accessToken) {
        this.accessToken = accessToken;
    }
}
