package com.lloll.myro.domain.account.user.application;

import com.lloll.myro.domain.account.dao.UserRepository;
import com.lloll.myro.domain.account.jwt.TokenProvider;
import com.lloll.myro.domain.account.user.domain.User;
import com.lloll.myro.domain.account.user.dto.UpdateUserRequest;
import com.lloll.myro.domain.account.user.dto.UserBillingResponse;
import com.lloll.myro.domain.account.user.dto.UserMyPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${jwt.ACCESS_TOKEN_MINUTE_TIME}")
    private int ACCESS_TOKEN_MINUTE_TIME;

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public User updateUser(UpdateUserRequest request, String token) {
        return null;
    }

    @Override
    public void deleteUser(String token) {

    }

    @Override
    public void logoutUser(String token) {

    }

    @Override
    public UserBillingResponse billingUser(String token) {
        return null;
    }

    @Override
    public UserMyPageResponse getUserInfo(String token) {
        return null;
    }
}