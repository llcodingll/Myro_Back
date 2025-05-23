package com.lloll.myro.domain.account.user.application;

import com.lloll.myro.domain.account.dao.UserRepository;
import com.lloll.myro.domain.account.jwt.Token;
import com.lloll.myro.domain.account.jwt.TokenProvider;
import com.lloll.myro.domain.account.user.domain.User;
import com.lloll.myro.domain.account.user.dto.LoginResponse;
import com.lloll.myro.domain.account.user.dto.LoginUserRequest;
import com.lloll.myro.domain.account.user.dto.RegisterUserRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class NonUserServiceImpl implements NonUserService {

    @Value("${jwt.ACCESS_TOKEN_MINUTE_TIME}")
    private int ACCESS_TOKEN_MINUTE_TIME;

    private final UserRepository userRepository;

    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public LoginResponse loginUser(LoginUserRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자가 존재하지 않습니다."));

        Token accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_MINUTE_TIME);

        return new LoginResponse(accessToken);
    }

    @Override
    public User saveUser(RegisterUserRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        });
        return userRepository.save(new User(request));
    }
}
