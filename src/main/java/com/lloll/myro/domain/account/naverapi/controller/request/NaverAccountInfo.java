package com.lloll.myro.domain.account.naverapi.controller.request;

import com.lloll.myro.domain.account.user.domain.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.Getter;

@Getter
public class NaverAccountInfo {
    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Email(message = "잘못된 이메일 형식입니다.")
    private String email;

    @NotBlank(message = "이름은 필수 항목입니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z]+$", message = "Name can only be entered in Korean or English.")
    @Size(max = 5)
    private String name;

    @Size(max = 10)
    @Pattern(regexp = "^[가-힣a-zA-Z\\s]+$", message = "Nickname can only be entered in Korean or English.")
    private String nickname;

    @NotNull(message = "성별은 필수 항목입니다.")
    private Gender gender;

    @NotNull(message = "생년월일은 필수 항목입니다.")
    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    private LocalDate birthDate;

    public NaverAccountInfo() {
    }

    public NaverAccountInfo(String email, String name, String nickname, String gender, String year, String date) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        if (gender.equals("M")){
            this.gender = Gender.MALE;
        }
        if (gender.equals("F")){
            this.gender = Gender.FEMALE;
        }
        String fullDate = String.format("%s-%s", year, date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.birthDate = LocalDate.parse(fullDate, formatter);
    }
}
