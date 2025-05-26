package com.lloll.myro.domain.account.admin.api;

import com.lloll.myro.domain.account.admin.application.NonAdminServiceImpl;
import com.lloll.myro.domain.account.admin.application.request.LoginAdminRequest;
import com.lloll.myro.domain.account.user.application.response.LoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class NonAdminController {
    private final NonAdminServiceImpl nonAdminService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginAdmin(@RequestBody @Valid LoginAdminRequest loginAdminRequest) {
        return ResponseEntity.ok().body(nonAdminService.login(loginAdminRequest));
    }
}