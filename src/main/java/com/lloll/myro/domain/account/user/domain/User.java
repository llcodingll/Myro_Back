package com.lloll.myro.domain.account.user.domain;

import com.lloll.myro.domain.account.domain.Role;
import com.lloll.myro.domain.account.kakaoapi.api.request.KakaoAccountInfo;
import com.lloll.myro.domain.account.naverapi.api.request.NaverAccountInfo;
import com.lloll.myro.domain.account.user.api.request.UpdateUserRequest;
import com.lloll.myro.domain.eventLog.domain.EventLog;
import com.lloll.myro.domain.notification.domain.Notification;
import com.lloll.myro.domain.schedule.domain.Schedule;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private long id;

    @Column(unique = true)
    private String name;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull
    private String nickname;

    private String phone;
    private LocalDate birthDate;

    private Role role;
    private Boolean isBilling;

    private LocalDate createdAt;
    private LocalDate updatedAt;
    private LocalDate deletedAt;

    @OneToMany
    private List<Schedule> schedules;

    @OneToMany
    private List<Notification> notifications;

    @OneToMany
    private List<EventLog> logs;

    public User(KakaoAccountInfo kakaoAccountInfo) {
        this.email = kakaoAccountInfo.getEmail();
        this.password = bCryptPasswordEncoder.encode(kakaoAccountInfo.getEmail());
        this.name = kakaoAccountInfo.getName();
        this.nickname = kakaoAccountInfo.getNickname();
        this.gender = kakaoAccountInfo.getGender();
        this.birthDate = kakaoAccountInfo.getBirthDate();
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
        this.role = Role.USER;
        this.isBilling = false;
    }

    public User(NaverAccountInfo naverAccountInfo) {
        this.email = naverAccountInfo.getEmail();
        this.password = bCryptPasswordEncoder.encode(naverAccountInfo.getEmail());
        this.name = naverAccountInfo.getName();
        this.nickname = naverAccountInfo.getNickname();
        this.gender = naverAccountInfo.getGender();
        this.birthDate = naverAccountInfo.getBirthDate();
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
        this.role = Role.USER;
        this.isBilling = false;
    }

    public void updateUserDetails(UpdateUserRequest request) {
        if (request.getName() != null) {
            this.name = request.getName();
        }
        if (request.getNickname() != null) {
            this.nickname = request.getNickname();
        }
        if (request.getGender() != null) {
            this.gender = request.getGender();
        }
        if (request.getBirthDate() != null) {
            this.birthDate = request.getBirthDate();
        }
        this.updatedAt = LocalDate.now();
    }
}
