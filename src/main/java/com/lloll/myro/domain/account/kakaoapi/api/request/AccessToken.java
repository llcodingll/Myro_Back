package com.lloll.myro.domain.account.kakaoapi.api.request;

import com.lloll.myro.domain.account.jwt.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccessToken {
    private Token accessToken;
}
