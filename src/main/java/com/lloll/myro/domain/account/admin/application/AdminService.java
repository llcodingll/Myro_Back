package com.lloll.myro.domain.account.admin.application;

import com.lloll.myro.domain.account.jwt.Token;
import com.lloll.myro.domain.account.user.domain.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {

    Page<User> findAll(Pageable pageable);
    Token getUserToken(Long userId);
    User findByEmail(String email);
    List<Object[]> getUserBillingCount();
}
