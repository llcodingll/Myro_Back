package com.lloll.myro.domain.account.admin.application;

import com.lloll.myro.domain.account.admin.application.request.LoginAdminRequest;
import com.lloll.myro.domain.account.admin.dao.AdminRepository;
import com.lloll.myro.domain.account.admin.domain.Admin;
import com.lloll.myro.domain.account.jwt.Token;
import com.lloll.myro.domain.account.jwt.TokenProvider;
import com.lloll.myro.domain.account.user.application.response.LoginResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class NonAdminServiceImpl implements NonAdminService{

    @Value("${jwt.ACCESS_TOKEN_MINUTE_TIME}")
    private int ACCESS_TOKEN_MINUTE_TIME;

    private final AdminRepository adminRepository;
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public LoginResponse login(LoginAdminRequest loginAdminRequest) {
        Admin admin = adminRepository.findByAdminCode(loginAdminRequest.getAdminCode()).orElseThrow();

        if (bCryptPasswordEncoder.encode(loginAdminRequest.getPassword()).equals(admin.getPassword())) {
            throw new IllegalStateException("Incorrect password");
        }

        Token accessToken = tokenProvider.generateToken(admin, ACCESS_TOKEN_MINUTE_TIME);

        return new LoginResponse(accessToken);
    }
}
