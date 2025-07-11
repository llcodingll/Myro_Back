package com.lloll.myro.domain.account.user.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.lloll.myro.domain.account.jwt.Token;
import com.lloll.myro.domain.account.jwt.domain.RefreshToken;
import com.lloll.myro.domain.account.user.UserServiceTestSupport;
import com.lloll.myro.domain.account.user.api.request.UpdateUserRequest;
import com.lloll.myro.domain.account.user.application.response.LoginResponse;
import com.lloll.myro.domain.account.user.application.response.UserBillingResponse;
import com.lloll.myro.domain.account.user.application.response.UserMyPageResponse;
import com.lloll.myro.domain.account.user.domain.Gender;
import com.lloll.myro.domain.account.user.domain.User;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

class UserServiceImplIntegrationTest extends UserServiceTestSupport {

    @Test
    @DisplayName("회원 정보를 수정하면 변경된 정보가 저장된다.")
    void updateUser_success() {
        //given
        String token = "validToken";
        Long userId = 1L;
        User user = createUserWithId("test@email.com", "pw", "OldName", "OldNick", Gender.FEMALE,
                LocalDate.of(1980, 1, 1), userId);

        UpdateUserRequest request = new UpdateUserRequest();
        request.setName("Hong Gil Dong");
        request.setNickname("GilDong");
        request.setGender(Gender.MALE);
        request.setBirthDate(LocalDate.of(1990, 1, 1));

        when(tokenProvider.getUserIdFromToken(token)).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        //when
        User updatedUser = userService.updateUser(request, token);

        //then
        assertThat(updatedUser.getName()).isEqualTo("Hong Gil Dong");
        assertThat(updatedUser.getNickname()).isEqualTo("GilDong");
        assertThat(updatedUser.getGender()).isEqualTo(Gender.MALE);
        assertThat(updatedUser.getBirthDate()).isEqualTo(LocalDate.of(1990, 1, 1));
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("정지된 회원은 정보를 수정할 수 없다.")
    void updateUser_blockedUser_throwsException() {
        //given
        String token = "validToken";
        Long userId = 1L;
        User user = createUserWithId("test@email.com", "pw", "Name", "Nick", Gender.MALE, LocalDate.of(1990, 1, 1),
                userId);
        setField(user, "deletedAt", LocalDate.now());

        UpdateUserRequest request = new UpdateUserRequest();

        when(tokenProvider.getUserIdFromToken(token)).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        //when //then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> userService.updateUser(request, token));
        assertEquals("정지된 사용자입니다. 관리자에게 문의하세요.", exception.getMessage());
    }

    @Test
    @DisplayName("회원 탈퇴를 하면 회원 정보와 리프레시 토큰이 모두 삭제된다.")
    void deleteUser_success() {
        //given
        String token = "validToken";
        Long userId = 1L;
        when(tokenProvider.getUserIdFromToken(token)).thenReturn(userId);

        //when
        userService.deleteUser(token);

        //then
        verify(refreshTokenRepository).deleteByUserId(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    @DisplayName("로그아웃을 하면 리프레시 토큰이 삭제된다.")
    void logoutUser_success() {
        //given
        String token = "refreshToken";
        //when
        userService.logoutUser(token);
        //then
        verify(refreshTokenRepository).deleteByRefreshToken(token);
    }

    @Test
    @DisplayName("빌링 정보를 조회하면 회원의 빌링 여부가 반환된다.")
    void billingUser_success() {
        //given
        String token = "validToken";
        Long userId = 1L;
        User user = createUserWithId("test@email.com", "pw", "Name", "Nick", Gender.FEMALE, LocalDate.of(1990, 1, 1),
                userId);
        setField(user, "isBilling", true);

        when(tokenProvider.getUserIdFromToken(token)).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        //when
        UserBillingResponse response = userService.billingUser(token);

        //then
        assertThat(response.isBilling()).isTrue();
    }

    @Test
    @DisplayName("마이페이지에서 회원 정보를 조회하면 저장된 회원 정보가 반환된다.")
    void getUserInfo_success() {
        //given
        String token = "validToken";
        Long userId = 1L;
        User user = createUserWithId("test@email.com", "pw", "Hong Gil Dong", "GilDong", Gender.MALE,
                LocalDate.of(1990, 1, 1), userId);

        when(tokenProvider.getUserIdFromToken(token)).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        //when
        UserMyPageResponse response = userService.getUserInfo(token);

        //then
        assertThat(response.getName()).isEqualTo("Hong Gil Dong");
        assertThat(response.getNickname()).isEqualTo("GilDong");
        assertThat(response.getEmail()).isEqualTo("test@email.com");
        assertThat(response.getGender()).isEqualTo(Gender.MALE);
        assertThat(response.getBirthDay()).isEqualTo(LocalDate.of(1990, 1, 1));
    }

    @Test
    @DisplayName("리프레시 토큰으로 토큰을 재발급하면 새로운 액세스 토큰과 리프레시 토큰이 발급된다.")
    void refreshToken_success() {
        //given
        String refreshToken = "refreshToken";
        Long userId = 1L;
        User user = createUserWithId("test@email.com", "pw", "Name", "Nick", Gender.FEMALE, LocalDate.of(1990, 1, 1),
                userId);

        Token newAccessToken = new Token("newAccess");
        Token newRefreshToken = new Token("newRefresh");

        when(refreshTokenRepository.findByRefreshToken(refreshToken)).thenReturn(Optional.of(new RefreshToken()));
        when(tokenProvider.getUserIdFromToken(refreshToken)).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(tokenProvider.generateToken(user, 0)).thenReturn(newAccessToken).thenReturn(newRefreshToken);

        //when
        LoginResponse response = userService.refreshToken(refreshToken);

        //then
        assertThat(response.getAccessToken().getToken()).isEqualTo("newAccess");
        assertThat(response.getRefreshToken().getToken()).isEqualTo("newRefresh");
        verify(refreshTokenRepository).deleteByRefreshToken(refreshToken);
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("전체 회원을 페이지 단위로 조회하면 해당 페이지의 회원 목록이 반환된다.")
    void findAll_success() {
        //given
        PageRequest pageable = PageRequest.of(0, 2);
        User user1 = createUserWithId("a@email.com", "pw", "A", "A", Gender.MALE, LocalDate.of(1990, 1, 1), 1L);
        User user2 = createUserWithId("b@email.com", "pw", "B", "B", Gender.FEMALE, LocalDate.of(1991, 2, 2), 2L);
        List<User> users = Arrays.asList(user1, user2);
        Page<User> page = new PageImpl<>(users, pageable, users.size());

        when(userRepository.findAll(pageable)).thenReturn(page);

        //when
        Page<User> result = userService.findAll(pageable);

        //then
        assertThat(result.getContent()).containsExactly(user1, user2);
    }

    @Test
    @DisplayName("회원 ID로 토큰을 발급하면 해당 회원의 토큰이 반환된다.")
    void getUserToken_success() {
        //given
        Long userId = 1L;
        User user = createUserWithId("test@email.com", "pw", "Name", "Nick", Gender.FEMALE, LocalDate.of(1990, 1, 1),
                userId);

        Token token = new Token("tokenValue");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(tokenProvider.generateToken(user, 0)).thenReturn(token);

        //when
        Token result = userService.getUserToken(userId);

        //then
        assertThat(result.getToken()).isEqualTo("tokenValue");
    }

    @Test
    @DisplayName("존재하지 않는 회원 ID로 토큰을 발급하면 예외가 발생한다.")
    void getUserToken_notFound_throwsException() {
        //given
        Long userId = 100L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //when //then
        assertThrows(NoSuchElementException.class, () -> userService.getUserToken(userId));
    }

    @Test
    @DisplayName("이메일로 회원을 조회하면 해당 회원이 반환된다.")
    void findByEmail_success() {
        //given
        String email = "test@email.com";
        User user = createUserWithId(email, "pw", "Name", "Nick", Gender.FEMALE, LocalDate.of(1990, 1, 1), 1L);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        //when
        User result = userService.findByEmail(email);

        //then
        assertThat(result).isEqualTo(user);
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 회원을 조회하면 예외가 발생한다.")
    void findByEmail_notFound_throwsException() {
        //given
        String email = "notfound@email.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        //when //then
        assertThrows(NoSuchElementException.class, () -> userService.findByEmail(email));
    }

    @Test
    @DisplayName("빌링 회원 수를 조회하면 결과가 반환된다.")
    void getUserBillingCount_success() {
        //given
        List<Object[]> mockResult = Collections.singletonList(new Object[]{1L, true});
        when(userRepository.countUserByBilling()).thenReturn(mockResult);

        //when
        List<Object[]> result = userService.getUserBillingCount();

        //then
        assertThat(result).isEqualTo(mockResult);
    }

    @Test
    @DisplayName("만료된 리프레시 토큰을 삭제하면 저장소의 삭제 메서드가 호출된다.")
    void deleteExpiredTokens_success() {
        //given //when
        userService.deleteExpiredTokens();
        //then
        verify(refreshTokenRepository).deleteExpiredTokens();
    }

}
