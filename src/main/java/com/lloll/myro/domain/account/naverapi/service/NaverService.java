package com.lloll.myro.domain.account.naverapi.service;

import com.lloll.myro.domain.account.naverapi.controller.request.NaverAccountInfo;
import com.lloll.myro.domain.account.user.application.UserServiceImpl;
import com.lloll.myro.domain.account.user.application.response.LoginResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class NaverService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final UserServiceImpl userService;

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    @Value("${naver.redirect-uri}")
    private String redirectUri;

    public LoginResponse handleNaverLogin(String code, String state) {
        String tokenUrl = "https://nid.naver.com/oauth2.0/token";
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(tokenUrl)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("code", code)
                .queryParam("state", state);

        ResponseEntity<Map<String, Object>> tokenResponse = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.POST,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        String accessToken = (String) tokenResponse.getBody().get("access_token");

        String userInfoUrl = "https://openapi.naver.com/v1/nid/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Map<String, Object>> userInfoResponse = restTemplate.exchange(
                userInfoUrl,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        Map<String, Object> userInfo = userInfoResponse.getBody();
        String email = null, name = null, nickname = null, gender = null, year = null, birthday = null;

        Object responseObj = userInfo.get("response");
        if (responseObj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = (Map<String, Object>) responseObj;
            email = (String) response.get("email");
            name = (String) response.get("name");
            nickname = (String) response.get("nickname");
            gender = (String) response.get("gender");
            year = (String) response.get("birthyear");
            birthday = (String) response.get("birthday"); // "MM-DD" 형식
        } else {
            throw new IllegalArgumentException("네이버 응답에서 사용자 정보를 찾을 수 없습니다.");
        }

        NaverAccountInfo naverAccountInfo = new NaverAccountInfo(email, name, nickname, gender, year, birthday);
        if (userService.naverUserCheck(email)) {
            return userService.naverLoginUser(email);
        } else {
            return userService.registerNaverUser(naverAccountInfo);
        }
    }
}