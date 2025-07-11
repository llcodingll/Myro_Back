package com.lloll.myro.domain.account.user.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    @DisplayName("네이버 계정이 이미 있으면 true, 없으면 false를 반환한다.")
    void naverUserCheck_trueAndFalse() {
        //given
        String email = "test@naver.com";
        NaverAccountInfo naverInfo = new NaverAccountInfo(
                email, "홍길동", "길동", "M", "1990", "01-01"
        );
        User user = new User(naverInfo);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        //when //then
        assertThat(userService.naverUserCheck(email)).isTrue();

        //given
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        //when //then
        assertThat(userService.naverUserCheck(email)).isFalse();
    }

    @Test
    @DisplayName("네이버 로그인 시 정상 회원이면 로그인 응답이 반환된다.")
    void naverLoginUser_success() {
        //given
        String email = "test@naver.com";
        NaverAccountInfo naverInfo = new NaverAccountInfo(
                email, "홍길동", "길동", "M", "1990", "01-01"
        );
        User user = new User(naverInfo);
        setField(user, "deletedAt", null);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        doReturn(new LoginResponse()).when(userRepository).findByEmail(email);

        //when
        LoginResponse response = userService.naverLoginUser(email);

        //then
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("네이버 로그인 시 정지 회원이면 예외가 발생한다.")
    void naverLoginUser_blocked() {
        //given
        String email = "test@naver.com";
        NaverAccountInfo naverInfo = new NaverAccountInfo(
                email, "홍길동", "길동", "M", "1990", "01-01"
        );
        User user = new User(naverInfo);
        setField(user, "deletedAt", LocalDate.now());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        //when //then
        assertThatThrownBy(() -> userService.naverLoginUser(email))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("카카오 계정이 이미 있으면 true, 없으면 false를 반환한다.")
    void kakaoUserCheck_trueAndFalse() {
        //given
        String email = "test@kakao.com";
        KakaoAccountInfo kakaoInfo = new KakaoAccountInfo(
                email, "홍길동", "길동", Gender.MALE, LocalDate.of(1990, 1, 1)
        );
        User user = new User(kakaoInfo);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        //when //then
        assertThat(userService.kakaoUserCheck(email)).isTrue();

        //given
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        //when //then
        assertThat(userService.kakaoUserCheck(email)).isFalse();
    }

    @Test
    @DisplayName("카카오 로그인 시 정상 회원이면 로그인 응답이 반환된다.")
    void kakaoLoginUser_success() {
        //given
        String email = "test@kakao.com";
        KakaoAccountInfo kakaoInfo = new KakaoAccountInfo(
                email, "홍길동", "길동", Gender.MALE, LocalDate.of(1990, 1, 1)
        );
        User user = new User(kakaoInfo);
        setField(user, "deletedAt", null);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        doReturn(new LoginResponse()).when(userRepository).findByEmail(email);

        //when
        LoginResponse response = userService.kakaoLoginUser(email);

        //then
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("카카오 로그인 시 정지 회원이면 예외가 발생한다.")
    void kakaoLoginUser_blocked() {
        //given
        String email = "test@kakao.com";
        KakaoAccountInfo kakaoInfo = new KakaoAccountInfo(
                email, "홍길동", "길동", Gender.MALE, LocalDate.of(1990, 1, 1)
        );
        User user = new User(kakaoInfo);
        setField(user, "deletedAt", LocalDate.now());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        //when //then
        assertThatThrownBy(() -> userService.kakaoLoginUser(email))
                .isInstanceOf(IllegalStateException.class);
    }

}
