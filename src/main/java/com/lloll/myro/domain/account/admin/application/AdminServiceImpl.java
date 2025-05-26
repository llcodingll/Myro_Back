package com.lloll.myro.domain.account.admin.application;

import com.lloll.myro.domain.account.admin.dao.AdminRepository;
import com.lloll.myro.domain.account.jwt.Token;
import com.lloll.myro.domain.account.user.application.UserServiceImpl;
import com.lloll.myro.domain.account.user.domain.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    @Value("${jwt.ACCESS_TOKEN_MINUTE_TIME}")
    private int ACCESS_TOKEN_MINUTE_TIME;

    private final AdminRepository adminRepository;
    private final UserServiceImpl userService;

    @Override
    public Page<User> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Token getUserToken(Long userId) {
        return null;
    }
}
