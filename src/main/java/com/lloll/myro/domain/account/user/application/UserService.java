package com.lloll.myro.domain.account.user.application;

import com.lloll.myro.domain.account.user.domain.User;
import com.lloll.myro.domain.account.user.application.request.UpdateUserRequest;
import com.lloll.myro.domain.account.user.application.response.UserBillingResponse;
import com.lloll.myro.domain.account.user.application.response.UserMyPageResponse;

public interface UserService {

    User updateUser(UpdateUserRequest request, String token);
    void deleteUser(String token);
    void logoutUser(String token);
    UserBillingResponse billingUser(String token);
    UserMyPageResponse getUserInfo(String token);
}