package com.lloll.myro.domain.account.user.dto;

import com.lloll.myro.domain.account.domain.Role;
import com.lloll.myro.domain.account.user.domain.Gender;
import com.lloll.myro.domain.account.user.domain.User;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class UserResponse {
    private final Long id;
    private final String email;
    private final String nickname;
    private final Gender gender;
    private final LocalDate birthDate;
    private final LocalDate createDate;
    private final Role role;
    private final Boolean isBilling;

    public UserResponse(User user) {
        this.id = user.getId();
        this.email = maskEmail(user.getEmail());
        this.nickname = user.getNickname();
        this.gender = user.getGender();
        this.birthDate = user.getBirthDate();
        this.createDate = user.getCreatedAt();
        this.role = user.getRole();
        this.isBilling = user.getIsBilling();
    }

    private static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }
        String[] parts = email.split("@");
        String localPart = parts[0];
        String domainPart = parts[1];

        if (localPart.length() <= 3) {
            return "***@" + domainPart;
        }
        return localPart.substring(0, 3) + "****@" + domainPart;
    }
}
