package com.lloll.myro.domain.account.user.application;

import com.lloll.myro.domain.account.dao.UserRepository;
import com.lloll.myro.domain.account.jwt.Token;
import com.lloll.myro.domain.account.jwt.TokenProvider;
import com.lloll.myro.domain.account.user.domain.User;
import com.lloll.myro.domain.account.user.application.response.LoginResponse;
import com.lloll.myro.domain.account.user.application.request.LoginUserRequest;
import com.lloll.myro.domain.account.user.application.request.RegisterUserRequest;
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

        validateUser(user, request.getPassword());

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

    private void validateUser(User user, String rawPassword) {
        if (user.getDeletedAt() != null) {
            throw new IllegalStateException("정지된 사용자입니다. 관리자에게 문의하세요.");
        }
        if (!isPasswordMatch(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    public boolean isPasswordMatch(String rawPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }
}
