package com.lloll.myro.domain.user.dao;

import com.lloll.myro.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
