package com.lloll.myro.domain.account.user.application;

import com.lloll.myro.domain.account.jwt.Token;
import com.lloll.myro.domain.account.user.application.request.UpdateUserRequest;
import com.lloll.myro.domain.account.user.application.response.UserBillingResponse;
import com.lloll.myro.domain.account.user.application.response.UserMyPageResponse;
import com.lloll.myro.domain.account.user.domain.User;
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
}