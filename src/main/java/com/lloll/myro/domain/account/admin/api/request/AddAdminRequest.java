package com.lloll.myro.domain.account.admin.api.request;

import com.lloll.myro.domain.account.domain.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AddAdminRequest {

    @NotBlank(message = "Admin Code can not be null")
    private String adminCode;

    @NotBlank(message = "Password can not be null")
    @Size(min = 8, max = 18, message = "The password is 8 characters or more and 18 characters or less.")
    private String password;

    @NotBlank(message = "Name can not be null")
    @Pattern(regexp = "^[가-힣a-zA-Z]+$", message = "Name can only be entered in Korean or English.")
    private String name;

    private Role role;

    public AddAdminRequest() {
    }

    public AddAdminRequest(String adminCode, String password, String name, Role role) {
        this.adminCode = adminCode;
        this.password = password;
        this.name = name;
        this.role = role;
    }

}
