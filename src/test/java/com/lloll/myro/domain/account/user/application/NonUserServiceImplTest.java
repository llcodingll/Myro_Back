package com.lloll.myro.domain.account.user.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.lloll.myro.domain.account.jwt.RefreshTokenRepository;
import com.lloll.myro.domain.account.jwt.Token;
import com.lloll.myro.domain.account.jwt.TokenProvider;
import com.lloll.myro.domain.account.jwt.domain.RefreshToken;
import com.lloll.myro.domain.account.user.api.request.LoginUserRequest;
import com.lloll.myro.domain.account.user.api.request.RegisterUserRequest;
import com.lloll.myro.domain.account.user.application.response.LoginResponse;
import com.lloll.myro.domain.account.user.dao.UserRepository;
import com.lloll.myro.domain.account.user.domain.Gender;
import com.lloll.myro.domain.account.user.domain.User;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class NonUserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private NonUserServiceImpl nonUserService;

    enum UserMockType { ACTIVE, DELETED }

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(nonUserService, "ACCESS_TOKEN_MINUTE_TIME", 10);
        ReflectionTestUtils.setField(nonUserService, "REFRESH_TOKEN_MINUTE_TIME", 60);
    }

    private User mockUser(String email, String encodedPassword, UserMockType type) {
        User user = mock(User.class);
        if (encodedPassword != null) {
            when(user.getPassword()).thenReturn(encodedPassword);
        }
        when(user.getDeletedAt()).thenReturn(type == UserMockType.DELETED ? LocalDate.now() : null);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        return user;
    }

    private void mockTokenProvider(User user, Token accessToken, Token refreshToken) {
        when(tokenProvider.generateToken(user, 10)).thenReturn(accessToken);
        when(tokenProvider.generateToken(user, 60)).thenReturn(refreshToken);
    }

    private RegisterUserRequest createRegisterUserRequest(String email) {
        return new RegisterUserRequest(
                email, "pw123456", "홍길동", "길동", Gender.MALE, LocalDate.of(1995, 1, 1)
        );
    }

    @Nested
    @DisplayName("회원 로그인 정책")
    class LoginPolicy {

        @Test
        @DisplayName("존재하는 활성 회원이 올바른 비밀번호로 로그인하면 액세스/리프레시 토큰이 발급된다")
        void loginSuccess_whenActiveUserAndCorrectPassword() {
            String email = "user@domain.com";
            String rawPassword = "correctPW";
            String encodedPassword = "$2a$10$encoded";
            User user = mockUser(email, encodedPassword, UserMockType.ACTIVE);
            when(bCryptPasswordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

            Token accessToken = new Token("access-token");
            Token refreshToken = new Token("refresh-token");
            mockTokenProvider(user, accessToken, refreshToken);

            LoginUserRequest request = new LoginUserRequest(email, rawPassword);
            LoginResponse response = nonUserService.loginUser(request);

            assertThat(response.getAccessToken().getToken()).isEqualTo("access-token");
            assertThat(response.getRefreshToken().getToken()).isEqualTo("refresh-token");
            verify(refreshTokenRepository).save(any(RefreshToken.class));
        }

        @Test
        @DisplayName("존재하지 않는 이메일로 로그인 시도 시 '존재하지 않는 사용자' 예외가 발생한다")
        void loginFail_whenEmailDoesNotExist() {
            String email = "notfound@domain.com";
            when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
            LoginUserRequest request = new LoginUserRequest(email, "pw");

            assertThatThrownBy(() -> nonUserService.loginUser(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("해당 이메일의 사용자가 존재하지 않습니다.");
        }

        @Test
        @DisplayName("정지 또는 탈퇴된 회원이 로그인 시도 시 '정지된 사용자' 예외가 발생한다")
        void loginFail_whenUserIsSuspendedOrDeleted() {
            String email = "deleted@domain.com";
            mockUser(email, null, UserMockType.DELETED);
            LoginUserRequest request = new LoginUserRequest(email, "pw");

            assertThatThrownBy(() -> nonUserService.loginUser(request))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("정지된 사용자입니다");
        }

        @Test
        @DisplayName("비밀번호가 일치하지 않으면 '비밀번호 불일치' 예외가 발생한다")
        void loginFail_whenPasswordIsIncorrect() {
            String email = "user@domain.com";
            String rawPassword = "wrongPW";
            String encodedPassword = "$2a$10$encoded";
            User user = mockUser(email, encodedPassword, UserMockType.ACTIVE);
            when(bCryptPasswordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

            LoginUserRequest request = new LoginUserRequest(email, rawPassword);

            assertThatThrownBy(() -> nonUserService.loginUser(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("비밀번호가 일치하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("회원가입 정책")
    class RegisterPolicy {

        @Test
        @DisplayName("신규 이메일로 회원가입하면 회원 정보가 저장된다")
        void registerSuccess_whenNewEmail() {
            String email = "new@domain.com";
            RegisterUserRequest req = createRegisterUserRequest(email);
            when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
            User savedUser = new User(req);
            when(userRepository.save(any(User.class))).thenReturn(savedUser);

            User result = nonUserService.saveUser(req);

            assertThat(result.getEmail()).isEqualTo(email);
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("이미 등록된 이메일로 회원가입 시도 시 '이미 존재하는 이메일' 예외가 발생한다")
        void registerFail_whenEmailAlreadyExists() {
            String email = "dup@domain.com";
            RegisterUserRequest req = createRegisterUserRequest(email);
            when(userRepository.findByEmail(email)).thenReturn(Optional.of(mock(User.class)));

            assertThatThrownBy(() -> nonUserService.saveUser(req))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("이미 존재하는 이메일입니다.");
        }
    }
}
