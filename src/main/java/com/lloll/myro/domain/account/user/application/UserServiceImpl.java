package com.lloll.myro.domain.account.user.application;

import com.lloll.myro.domain.account.dao.UserRepository;
import com.lloll.myro.domain.account.jwt.TokenProvider;
import com.lloll.myro.domain.account.user.domain.User;
import com.lloll.myro.domain.account.user.application.request.UpdateUserRequest;
import com.lloll.myro.domain.account.user.application.response.UserBillingResponse;
import com.lloll.myro.domain.account.user.application.response.UserMyPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${jwt.ACCESS_TOKEN_MINUTE_TIME}")

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    @Override
    public User updateUser(UpdateUserRequest request, String token) {
        Long userId = tokenProvider.getUserIdFromToken(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("토큰에 대한 사용자를 찾을 수 없습니다. " + token));

        if (user.getDeletedAt() != null) {
            throw new IllegalStateException("정지된 사용자입니다. 관리자에게 문의하세요.");
        }

        user.updateUserDetails(request);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String token) {
        Long userId = tokenProvider.getUserIdFromToken(token);
        userRepository.deleteById(userId);
    }

    @Override
    public void logoutUser(String token) {

    }

    @Override
    public UserBillingResponse billingUser(String token) {
        Long userId = tokenProvider.getUserIdFromToken(token);

        return userRepository.findById(userId).map(user -> new UserBillingResponse(user.getIsBilling()))
                .orElseThrow(() -> new IllegalArgumentException("토큰에 대한 사용자를 찾을 수 없습니다. " + token));
    }

    @Override
    public UserMyPageResponse getUserInfo(String token) {
        Long userIdFromToken = tokenProvider.getUserIdFromToken(token);
        User user = userRepository.findById(userIdFromToken).orElseThrow();
        return new UserMyPageResponse(user);
    }
}