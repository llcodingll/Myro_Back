package com.lloll.myro.domain.account.user.api;

import com.lloll.myro.domain.account.jwt.TokenProvider;
import com.lloll.myro.domain.account.user.application.UserServiceImpl;
import com.lloll.myro.domain.account.user.domain.User;
import com.lloll.myro.domain.account.user.application.request.UpdateUserRequest;
import com.lloll.myro.domain.account.user.application.response.UserBillingResponse;
import com.lloll.myro.domain.account.user.application.response.UserMyPageResponse;
import com.lloll.myro.domain.account.user.application.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserServiceImpl userService;
    private final TokenProvider tokenProvider;

    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateUser(@RequestHeader HttpHeaders headers,
                                                   @RequestBody @Valid UpdateUserRequest request) {
        String token = tokenProvider.getToken(headers);
        User user = userService.updateUser(request, token);
        return ResponseEntity.ok().body(new UserResponse(user));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestHeader HttpHeaders headers) {
        String token = tokenProvider.getToken(headers);
        userService.deleteUser(token);
        return ResponseEntity.ok().body("사용자가 성공적으로 삭제되었습니다.");
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logoutUser(@RequestHeader HttpHeaders headers) {
        String token = tokenProvider.getToken(headers);
        userService.logoutUser(token);
        return ResponseEntity.ok().body("로그아웃 되었습니다.");
    }

    @GetMapping("/billing")
    public ResponseEntity<UserBillingResponse> billingState(@RequestHeader HttpHeaders headers) {
        String token = tokenProvider.getToken(headers);
        return ResponseEntity.ok().body(userService.billingUser(token));
    }

    @GetMapping("/me")
    public ResponseEntity<UserMyPageResponse> myPage(@RequestHeader HttpHeaders headers) {
        String token = tokenProvider.getToken(headers);
        return ResponseEntity.ok().body(userService.getUserInfo(token));
    }
}