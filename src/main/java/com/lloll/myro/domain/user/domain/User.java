package com.lloll.myro.domain.user.domain;

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

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private long id;

    @Column(unique = true)
    private String name;

    @NotNull
    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull
    private String password;

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

}
