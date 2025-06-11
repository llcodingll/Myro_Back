package com.lloll.myro.domain.account.user.application;

import com.lloll.myro.domain.account.jwt.Token;
import com.lloll.myro.domain.account.kakaoapi.controller.request.KakaoAccountInfo;
import com.lloll.myro.domain.account.naverapi.controller.request.NaverAccountInfo;
import com.lloll.myro.domain.account.user.api.request.UpdateUserRequest;
import com.lloll.myro.domain.account.user.application.response.LoginResponse;
import com.lloll.myro.domain.account.user.application.response.UserBillingResponse;
import com.lloll.myro.domain.account.user.application.response.UserMyPageResponse;
import com.lloll.myro.domain.account.user.domain.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    User updateUser(UpdateUserRequest request, String token);
    void deleteUser(String token);
    void logoutUser(String token);
    UserBillingResponse billingUser(String token);
    UserMyPageResponse getUserInfo(String token);
    Page<User> findAll(Pageable pageable);
    Token getUserToken(Long userId);
    User findByEmail(String email);
    List<Object[]> getUserBillingCount();
    LoginResponse registerKakaoUser(KakaoAccountInfo kakaoAccountInfo);
    boolean naverUserCheck(String email);
    LoginResponse naverLoginUser(String email);
    LoginResponse registerNaverUser(NaverAccountInfo naverAccountInfo);
}