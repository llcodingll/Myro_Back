package com.lloll.myro.domain.account.user.application;

import com.lloll.myro.domain.account.user.domain.User;
import com.lloll.myro.domain.account.user.dto.LoginResponse;
import com.lloll.myro.domain.account.user.dto.LoginUserRequest;
import com.lloll.myro.domain.account.user.dto.RegisterUserRequest;

public interface NonUserService {
    LoginResponse loginUser(LoginUserRequest request);
    User saveUser(RegisterUserRequest request);
}
