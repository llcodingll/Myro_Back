package com.lloll.myro.domain.account.dao;

import com.lloll.myro.domain.account.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u.isBilling, COUNT(u) FROM User u GROUP BY u.isBilling")
    List<Object[]> countUserByBilling();
}
