package com.lloll.myro.domain.account.admin.dao;

import com.lloll.myro.domain.account.admin.domain.Admin;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByAdminCode(String adminCode);
}
