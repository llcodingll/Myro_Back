package com.lloll.myro.domain.account.admin.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class EmailRequest {
    @NotBlank
    private String email;
}
