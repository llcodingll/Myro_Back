package com.lloll.myro.domain.account.admin.application;

import com.lloll.myro.domain.account.admin.api.request.LoginAdminRequest;
import com.lloll.myro.domain.account.user.application.response.LoginResponse;

public interface NonAdminService {
    LoginResponse login(LoginAdminRequest loginAdminRequest);
}
