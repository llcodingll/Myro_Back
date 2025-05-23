package com.lloll.myro.domain.account.user.dao;

import com.lloll.myro.domain.account.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
