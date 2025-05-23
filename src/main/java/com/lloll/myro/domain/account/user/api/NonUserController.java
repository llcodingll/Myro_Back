package com.lloll.myro.domain.account.user.api;

import com.lloll.myro.domain.account.user.application.NonUserServiceImpl;
import com.lloll.myro.domain.account.user.dto.LoginResponse;
import com.lloll.myro.domain.account.user.dto.LoginUserRequest;
import com.lloll.myro.domain.account.user.dto.RegisterUserRequest;
import com.lloll.myro.domain.account.user.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class NonUserController {

    private NonUserServiceImpl nonUserService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody @Valid LoginUserRequest loginUserRequest) {
        return ResponseEntity.ok().body(nonUserService.loginUser(loginUserRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> saveUser(@RequestBody @Valid RegisterUserRequest request) {
        return ResponseEntity.ok().body(new UserResponse(nonUserService.saveUser(request)));
    }
}
