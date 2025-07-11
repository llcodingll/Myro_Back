package com.lloll.myro.domain.account.user.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import com.lloll.myro.domain.account.kakaoapi.api.request.KakaoAccountInfo;
import com.lloll.myro.domain.account.naverapi.api.request.NaverAccountInfo;
import com.lloll.myro.domain.account.user.application.response.LoginResponse;
import com.lloll.myro.domain.account.user.domain.Gender;
import com.lloll.myro.domain.account.user.domain.User;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserServiceImplSocialIntegrationTest extends UserServiceTestSupport {

    @Test
    @DisplayName("네이버 계정 정보로 회원가입하면 신규 회원이면 회원가입과 로그인이, 기존 회원이면 로그인이 된다.")
    void registerNaverUser_newAndExisting() {
        NaverAccountInfo info = new NaverAccountInfo("test@naver.com", "홍길동", "길동", "M", "1990", "01-01");
        User user = new User(info);

        // 기존 회원
        when(userRepository.findByEmail(info.getEmail())).thenReturn(Optional.of(user));
        doReturn(new LoginResponse()).when(userRepository).findByEmail(info.getEmail());
        LoginResponse response1 = userService.registerNaverUser(info);
        assertThat(response1).isNotNull();

        // 신규 회원
        when(userRepository.findByEmail(info.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        doReturn(new LoginResponse()).when(userRepository).findByEmail(info.getEmail());
        LoginResponse response2 = userService.registerNaverUser(info);
        assertThat(response2).isNotNull();
    }

    @Test
    @DisplayName("네이버 계정이 이미 있으면 true, 없으면 false를 반환한다.")
    void naverUserCheck_trueAndFalse() {
        String email = "test@naver.com";
        NaverAccountInfo naverInfo = new NaverAccountInfo(
                email, "홍길동", "길동", "M", "1990", "01-01"
        );
        User user = new User(naverInfo);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        assertThat(userService.naverUserCheck(email)).isTrue();

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThat(userService.naverUserCheck(email)).isFalse();
    }

    @Test
    @DisplayName("네이버 로그인 시 정상 회원이면 로그인 응답이 반환된다.")
    void naverLoginUser_success() {
        String email = "test@naver.com";
        NaverAccountInfo naverInfo = new NaverAccountInfo(
                email, "홍길동", "길동", "M", "1990", "01-01"
        );
        User user = new User(naverInfo);
        setField(user, "deletedAt", null);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        doReturn(new LoginResponse()).when(userRepository).findByEmail(email);
        LoginResponse response = userService.naverLoginUser(email);
        assertThat(response).isNotNull();
    }


    @Test
    @DisplayName("네이버 로그인 시 정지 회원이면 예외가 발생한다.")
    void naverLoginUser_blocked() {
        String email = "test@naver.com";
        NaverAccountInfo naverInfo = new NaverAccountInfo(
                email, "홍길동", "길동", "M", "1990", "01-01"
        );
        User user = new User(naverInfo);
        setField(user, "deletedAt", LocalDate.now());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        assertThatThrownBy(() -> userService.naverLoginUser(email))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("카카오 계정 정보로 회원가입하면 신규 회원이면 회원가입과 로그인이, 기존 회원이면 로그인이 된다.")
    void registerKakaoUser_newAndExisting() {
        KakaoAccountInfo info = new KakaoAccountInfo("test@kakao.com", "홍길동", "길동", Gender.MALE,
                LocalDate.of(1990, 1, 1));
        User user = new User(info);

        // 기존 회원
        when(userRepository.findByEmail(info.getEmail())).thenReturn(Optional.of(user));
        doReturn(new LoginResponse()).when(userRepository).findByEmail(info.getEmail());
        LoginResponse response1 = userService.registerKakaoUser(info);
        assertThat(response1).isNotNull();

        // 신규 회원
        when(userRepository.findByEmail(info.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        doReturn(new LoginResponse()).when(userRepository).findByEmail(info.getEmail());
        LoginResponse response2 = userService.registerKakaoUser(info);
        assertThat(response2).isNotNull();
    }

    @Test
    @DisplayName("카카오 계정이 이미 있으면 true, 없으면 false를 반환한다.")
    void kakaoUserCheck_trueAndFalse() {
        String email = "test@kakao.com";
        KakaoAccountInfo kakaoInfo = new KakaoAccountInfo(
                email, "홍길동", "길동", Gender.MALE, LocalDate.of(1990, 1, 1)
        );
        User user = new User(kakaoInfo);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        assertThat(userService.kakaoUserCheck(email)).isTrue();

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThat(userService.kakaoUserCheck(email)).isFalse();
    }

    @Test
    @DisplayName("카카오 로그인 시 정상 회원이면 로그인 응답이 반환된다.")
    void kakaoLoginUser_success() {
        String email = "test@kakao.com";
        KakaoAccountInfo kakaoInfo = new KakaoAccountInfo(
                email, "홍길동", "길동", Gender.MALE, LocalDate.of(1990, 1, 1)
        );
        User user = new User(kakaoInfo);
        setField(user, "deletedAt", null);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        doReturn(new LoginResponse()).when(userRepository).findByEmail(email);
        LoginResponse response = userService.kakaoLoginUser(email);
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("카카오 로그인 시 정지 회원이면 예외가 발생한다.")
    void kakaoLoginUser_blocked() {
        String email = "test@kakao.com";
        KakaoAccountInfo kakaoInfo = new KakaoAccountInfo(
                email, "홍길동", "길동", Gender.MALE, LocalDate.of(1990, 1, 1)
        );
        User user = new User(kakaoInfo);
        setField(user, "deletedAt", LocalDate.now());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        assertThatThrownBy(() -> userService.kakaoLoginUser(email))
                .isInstanceOf(IllegalStateException.class);
    }
    
}
