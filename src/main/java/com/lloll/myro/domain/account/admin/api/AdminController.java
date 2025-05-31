package com.lloll.myro.domain.account.admin.api;

import com.lloll.myro.domain.account.admin.application.AdminServiceImpl;
import com.lloll.myro.domain.account.admin.application.request.EmailRequest;
import com.lloll.myro.domain.account.jwt.Token;
import com.lloll.myro.domain.account.user.domain.User;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminServiceImpl adminService;

    @GetMapping("/users")
    public ResponseEntity<Page<User>> users(Pageable pageable) {
        return ResponseEntity.ok(adminService.findAll(pageable));
    }

    @GetMapping("users/{userId}")
    public ResponseEntity<Token> userToken(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok().body(adminService.getUserToken(userId));
    }

    @PostMapping("/users/email")
    public ResponseEntity<User> usersByEmail(@RequestBody @Valid EmailRequest email) {
        return ResponseEntity.ok().body(adminService.findByEmail(email.getEmail()));
    }

    @GetMapping("/users/billing/count")
    public ResponseEntity<List<Object[]>> userBillingCount() {
        return ResponseEntity.ok().body(adminService.getUserBillingCount());
    }
}
