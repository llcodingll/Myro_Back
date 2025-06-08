package com.lloll.myro.domain.account.user.application.request;

import com.lloll.myro.domain.account.user.domain.Gender;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
    @Pattern(regexp = "^[가-힣a-zA-Z]+$", message = "Name can only be entered in Korean or English.")
    private String name;

    @Pattern(regexp = "^[가-힣a-zA-Z\\s]+$", message = "Nickname can only be entered in Korean or English.")
    private String nickname;

    private Gender gender;

    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    private LocalDate birthDate;
}