package com.lloll.myro.domain.account.kakaoapi.controller;

import com.lloll.myro.domain.account.kakaoapi.service.KakaoService;
import com.lloll.myro.domain.account.kakaoapi.service.request.KakaoAccountInfo;
import com.lloll.myro.domain.account.user.application.UserServiceImpl;
import com.lloll.myro.domain.account.user.application.response.LoginResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/oauth/kakao")
public class KakaoController {

    private final KakaoService kakaoService;
    private final UserServiceImpl userService;

    @GetMapping("/callback")
    public void kakaoCallback(@RequestParam("code") String code, HttpServletResponse response) throws Exception {
        String accessToken = kakaoService.getAccessToken(code);
        Map<String, Object> userInfo = kakaoService.getUserInfo(accessToken);
        String email = extractEmail(userInfo);
        String nickname = extractNickname(userInfo);

        if (userService.kakaoUserCheck(email)) {
            redirectExistingUser(response, userService.kakaoLoginUser(email));
        } else {
            redirectNewUser(response, email, nickname);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> registerUser(@RequestBody @Valid KakaoAccountInfo kakaoAccountInfo) {
        return ResponseEntity.ok(userService.registerKakaoUser(kakaoAccountInfo));
    }

    private String extractEmail(Map<String, Object> userInfo) {
        Object kakaoAccountObj = userInfo.get("kakao_account");
        if (kakaoAccountObj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> kakaoAccount = (Map<String, Object>) kakaoAccountObj;
            return (String) kakaoAccount.get("email");
        }
        return null;
    }

    private String extractNickname(Map<String, Object> userInfo) {
        Object kakaoAccountObj = userInfo.get("kakao_account");
        if (kakaoAccountObj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> kakaoAccount = (Map<String, Object>) kakaoAccountObj;
            Object profileObj = kakaoAccount.get("profile");
            if (profileObj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> profile = (Map<String, Object>) profileObj;
                return profile.get("nickname") != null ? (String) profile.get("nickname") : "Unknown";
            }
        }
        return "Unknown";
    }

    private void redirectExistingUser(HttpServletResponse response, LoginResponse loginResponse) throws Exception {
        String redirectUrl = String.format(
                "https://repick.site/?accessToken=%s&refreshToken=%s",
                URLEncoder.encode(loginResponse.getAccessToken().getToken(), StandardCharsets.UTF_8),
                URLEncoder.encode(loginResponse.getRefreshToken().getToken(), StandardCharsets.UTF_8)
        );
        response.sendRedirect(redirectUrl);
    }

    private void redirectNewUser(HttpServletResponse response, String email, String nickname) throws Exception {
        String redirectUrl = String.format(
                "https://repick.site/complete-profile?email=%s&name=%s",
                URLEncoder.encode(email, StandardCharsets.UTF_8),
                URLEncoder.encode(nickname, StandardCharsets.UTF_8)
        );
        response.sendRedirect(redirectUrl);
    }
}
