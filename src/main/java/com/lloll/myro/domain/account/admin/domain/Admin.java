package com.lloll.myro.domain.account.admin.domain;

import com.lloll.myro.domain.account.admin.api.request.AddAdminRequest;
import com.lloll.myro.domain.account.admin.api.request.UpdateAdminRequest;
import com.lloll.myro.domain.account.domain.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@Setter
@Entity
@Table(name = "admins")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin {
    static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, updatable = false, nullable = false)
    private Long id;

    @Column(name = "admin_code", unique = true)
    private String adminCode;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "role")
    private Role role;

    public Admin(AddAdminRequest addAdminRequest) {
        this.adminCode = addAdminRequest.getAdminCode();
        this.name = addAdminRequest.getName();
        this.password = bCryptPasswordEncoder.encode(addAdminRequest.getPassword());
        this.role = addAdminRequest.getRole();
    }

    public void updateUserDetails(UpdateAdminRequest updateAdminRequest) {
        if (updateAdminRequest.getAdminCode() != null) {
            this.adminCode = updateAdminRequest.getAdminCode();
        }
        if (updateAdminRequest.getName() != null) {
            name = updateAdminRequest.getName();
        }
        if (updateAdminRequest.getRole() != null) {
            role = updateAdminRequest.getRole();
        }
    }

}
