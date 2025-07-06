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

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(nonUserService, "ACCESS_TOKEN_MINUTE_TIME", 10);
        ReflectionTestUtils.setField(nonUserService, "REFRESH_TOKEN_MINUTE_TIME", 60);
    }

    @Nested
    @DisplayName("회원 로그인 정책")
    class LoginPolicy {

        @Test
        @DisplayName("존재하는 활성 회원이 올바른 비밀번호로 로그인하면 액세스/리프레시 토큰이 발급된다")
        void loginSuccess_whenActiveUserAndCorrectPassword() {
            // given
            String email = "user@domain.com";
            String rawPassword = "correctPW";
            String encodedPassword = "$2a$10$encoded";
            User user = mock(User.class);
            when(user.getPassword()).thenReturn(encodedPassword);
            when(user.getDeletedAt()).thenReturn(null);
            when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
            when(bCryptPasswordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

            Token accessToken = new Token("access-token");
            Token refreshToken = new Token("refresh-token");
            when(tokenProvider.generateToken(user, 10)).thenReturn(accessToken);
            when(tokenProvider.generateToken(user, 60)).thenReturn(refreshToken);

            // when
            LoginUserRequest request = new LoginUserRequest(email, rawPassword);
            LoginResponse response = nonUserService.loginUser(request);

            // then
            assertThat(response.getAccessToken().getToken()).isEqualTo("access-token");
            assertThat(response.getRefreshToken().getToken()).isEqualTo("refresh-token");
            verify(refreshTokenRepository).save(any(RefreshToken.class));
        }

        @Test
        @DisplayName("존재하지 않는 이메일로 로그인 시도 시 '존재하지 않는 사용자' 예외가 발생한다")
        void loginFail_whenEmailDoesNotExist() {
            // given
            String email = "notfound@domain.com";
            when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
            LoginUserRequest request = new LoginUserRequest(email, "pw");

            // when & then
            assertThatThrownBy(() -> nonUserService.loginUser(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("해당 이메일의 사용자가 존재하지 않습니다.");
        }

        @Test
        @DisplayName("정지 또는 탈퇴된 회원이 로그인 시도 시 '정지된 사용자' 예외가 발생한다")
        void loginFail_whenUserIsSuspendedOrDeleted() {
            // given
            String email = "deleted@domain.com";
            User user = mock(User.class);
            when(user.getDeletedAt()).thenReturn(LocalDate.now());
            when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
            LoginUserRequest request = new LoginUserRequest(email, "pw");

            // when & then
            assertThatThrownBy(() -> nonUserService.loginUser(request))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("정지된 사용자입니다");
        }

        @Test
        @DisplayName("비밀번호가 일치하지 않으면 '비밀번호 불일치' 예외가 발생한다")
        void loginFail_whenPasswordIsIncorrect() {
            // given
            String email = "user@domain.com";
            String rawPassword = "wrongPW";
            User user = mock(User.class);
            when(user.getPassword()).thenReturn("$2a$10$encoded");
            when(user.getDeletedAt()).thenReturn(null);
            when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
            when(bCryptPasswordEncoder.matches(rawPassword, "$2a$10$encoded")).thenReturn(false);

            LoginUserRequest request = new LoginUserRequest(email, rawPassword);

            // when & then
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
            // given
            String email = "new@domain.com";
            RegisterUserRequest req = new RegisterUserRequest(
                    email, "pw123456", "홍길동", "길동", Gender.MALE, LocalDate.of(1995, 1, 1)
            );
            when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
            User savedUser = new User(req);
            when(userRepository.save(any(User.class))).thenReturn(savedUser);

            // when
            User result = nonUserService.saveUser(req);

            // then
            assertThat(result.getEmail()).isEqualTo(email);
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("이미 등록된 이메일로 회원가입 시도 시 '이미 존재하는 이메일' 예외가 발생한다")
        void registerFail_whenEmailAlreadyExists() {
            // given
            String email = "dup@domain.com";
            RegisterUserRequest req = new RegisterUserRequest(
                    email, "pw123456", "홍길동", "길동", Gender.MALE, LocalDate.of(1995, 1, 1)
            );
            when(userRepository.findByEmail(email)).thenReturn(Optional.of(mock(User.class)));

            // when & then
            assertThatThrownBy(() -> nonUserService.saveUser(req))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("이미 존재하는 이메일입니다.");
        }
    }
}
