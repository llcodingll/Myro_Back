package com.lloll.myro.domain.account.user.application;

import com.lloll.myro.domain.account.user.domain.User;
import com.lloll.myro.domain.account.user.application.response.LoginResponse;
import com.lloll.myro.domain.account.user.application.request.LoginUserRequest;
import com.lloll.myro.domain.account.user.application.request.RegisterUserRequest;

public interface NonUserService {
    LoginResponse loginUser(LoginUserRequest request);
    User saveUser(RegisterUserRequest request);
}
