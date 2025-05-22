package com.lloll.myro.domain.account.dao;

import com.lloll.myro.domain.account.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
