package com.lloll.myro.domain.account.admin.application.request;

import com.lloll.myro.domain.account.domain.Role;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UpdateAdminRequest {

    @NotNull
    private Long adminId;

    private String adminCode;

    @Pattern(regexp = "^[가-힣a-zA-Z]+$", message = "Name can only be entered in Korean or English.")
    private String name;

    private Role role;

    public UpdateAdminRequest() {
    }

    public UpdateAdminRequest(Long adminId, String adminCode, String name, Role role) {
        this.adminId = adminId;
        this.adminCode = adminCode;
        this.name = name;
        this.role = role;
    }
}
