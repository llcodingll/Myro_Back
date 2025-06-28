package com.lloll.myro.domain.account.admin.api;

import com.lloll.myro.domain.account.admin.application.SuperAdminServiceImpl;
import com.lloll.myro.domain.account.admin.api.request.AddAdminRequest;
import com.lloll.myro.domain.account.admin.api.request.DeleteAdminRequest;
import com.lloll.myro.domain.account.admin.api.request.UpdateAdminRequest;
import com.lloll.myro.domain.account.admin.domain.Admin;
import com.lloll.myro.domain.account.jwt.TokenProvider;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/super")
public class SuperAdminController {

    private final SuperAdminServiceImpl superAdminService;
    private final TokenProvider tokenProvider;

    @GetMapping("/info")
    public ResponseEntity<List<Admin>> info() {
        return ResponseEntity.ok().body(superAdminService.findAllAdmins());
    }

    @PostMapping("/signup")
    public ResponseEntity<Admin> createAdmin(@RequestBody @Valid AddAdminRequest request) {
        return ResponseEntity.ok().body(superAdminService.saveAdmin(request));
    }

    @PutMapping("/update")
    public ResponseEntity<Admin> updateAdmin(@RequestHeader HttpHeaders headers,
                                             @RequestBody @Valid UpdateAdminRequest request) {
        String token = tokenProvider.getToken(headers);
        return ResponseEntity.ok().body(superAdminService.updateAdmin(token, request));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAdmin(@RequestHeader HttpHeaders headers,
                                              @RequestBody @Valid DeleteAdminRequest request) {
        String token = tokenProvider.getToken(headers);
        superAdminService.deleteAdmin(token, request);
        return ResponseEntity.ok().body("Deleted Admin");
    }

}
