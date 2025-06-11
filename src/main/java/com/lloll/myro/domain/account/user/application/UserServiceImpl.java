package com.lloll.myro.domain.account.user.application;

import com.lloll.myro.domain.account.jwt.RefreshTokenRepository;
import com.lloll.myro.domain.account.jwt.Token;
import com.lloll.myro.domain.account.jwt.TokenProvider;
import com.lloll.myro.domain.account.jwt.domain.RefreshToken;
import com.lloll.myro.domain.account.kakaoapi.controller.request.KakaoAccountInfo;
import com.lloll.myro.domain.account.naverapi.controller.request.NaverAccountInfo;
import com.lloll.myro.domain.account.user.api.request.UpdateUserRequest;
import com.lloll.myro.domain.account.user.application.response.LoginResponse;
import com.lloll.myro.domain.account.user.application.response.UserBillingResponse;
import com.lloll.myro.domain.account.user.application.response.UserMyPageResponse;
import com.lloll.myro.domain.account.user.dao.UserActivityLogRepository;
import com.lloll.myro.domain.account.user.dao.UserRepository;
import com.lloll.myro.domain.account.user.domain.User;
import com.lloll.myro.domain.account.user.domain.UserActivityLog;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("${jwt.ACCESS_TOKEN_MINUTE_TIME}")
    private int ACCESS_TOKEN_MINUTE_TIME;

    @Value("${jwt.REFRESH_TOKEN_MINUTE_TIME}")
    private int REFRESH_TOKEN_MINUTE_TIME;

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserActivityLogRepository userActivityLogRepository;


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
        refreshTokenRepository.deleteByUserId(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public void logoutUser(String token) {
        refreshTokenRepository.deleteByRefreshToken(token);
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

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Token getUserToken(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return tokenProvider.generateToken(user, ACCESS_TOKEN_MINUTE_TIME);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(null);
    }

    @Override
    public List<Object[]> getUserBillingCount() {
        return userRepository.countUserByBilling();
    }

    @Override
    public LoginResponse registerKakaoUser(KakaoAccountInfo kakaoAccountInfo) {
        return userRepository.findByEmail(kakaoAccountInfo.getEmail())
                .map(existingUser -> kakaoLoginUser(existingUser.getEmail())).orElseGet(() -> {
                    userRepository.save(new User(kakaoAccountInfo));
                    return kakaoLoginUser(kakaoAccountInfo.getEmail());
                });
    }

    public LoginResponse refreshToken(String refreshToken) {
        refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다."));

        refreshTokenRepository.deleteByRefreshToken(refreshToken);

        User user = userRepository.findById(tokenProvider.getUserIdFromToken(refreshToken))
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        Token newAccessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_MINUTE_TIME);
        Token newRefreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_MINUTE_TIME);

        refreshTokenRepository.save(new RefreshToken(user, newRefreshToken.getToken(),
                LocalDateTime.now().plusMinutes(REFRESH_TOKEN_MINUTE_TIME)));
        logger.info("LoginResponse: accessToken={}, refreshToken={}", newAccessToken.getToken(), newRefreshToken.getToken());
        return new LoginResponse(newAccessToken, newRefreshToken);
    }

    public void deleteExpiredTokens() {
        refreshTokenRepository.deleteExpiredTokens();
    }

    static void existingLog(User user, UserActivityLogRepository userActivityLogRepository) {
        saveTodayActivityIfNotExists(user, userActivityLogRepository);
    }

    private static void saveTodayActivityIfNotExists(User user, UserActivityLogRepository userActivityLogRepository) {
        Optional<UserActivityLog> existingLog = userActivityLogRepository.findByUserIdAndActivityDate(user.getId(),
                LocalDate.now());

        if (existingLog.isEmpty()) {
            UserActivityLog log = new UserActivityLog();
            log.setUserId(user.getId());
            log.setActivityDate(LocalDate.now());
            userActivityLogRepository.save(log);
        }
    }

    public boolean kakaoUserCheck(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        return byEmail.isPresent();
    }

    public LoginResponse kakaoLoginUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        if (user.getDeletedAt() != null) {
            throw new IllegalStateException("정지된 사용자입니다. 관리자에게 문의하세요.");
        }
        userActiveLog(user);
        return getLoginResponse(email);
    }

    private void userActiveLog(User user) {
        saveTodayActivityIfNotExists(user, userActivityLogRepository);
    }

    private LoginResponse getLoginResponse(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자가 존재하지 않습니다."));

        Token accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_MINUTE_TIME);
        Token refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_MINUTE_TIME);

        refreshTokenRepository.save(new RefreshToken(user, refreshToken.getToken(),
                LocalDateTime.now().plusMinutes(REFRESH_TOKEN_MINUTE_TIME)));

        return new LoginResponse(accessToken, refreshToken);
    }

    @Override
    public boolean naverUserCheck(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        return byEmail.isPresent();
    }


    @Override
    public LoginResponse naverLoginUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        if (user.getDeletedAt() != null) {
            throw new IllegalStateException("정지된 사용자입니다. 관리자에게 문의하세요.");
        }
        userActiveLog(user);
        return getLoginResponse(email);
    }

    @Override
    public LoginResponse registerNaverUser(NaverAccountInfo naverAccountInfo) {
        return userRepository.findByEmail(naverAccountInfo.getEmail())
                .map(existingUser -> kakaoLoginUser(existingUser.getEmail())).orElseGet(() -> {
                    userRepository.save(new User(naverAccountInfo));
                    return naverLoginUser(naverAccountInfo.getEmail());
                });
    }

}