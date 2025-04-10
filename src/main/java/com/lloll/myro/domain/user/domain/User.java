package com.lloll.myro.domain.user.domain;

import com.lloll.myro.domain.eventLog.domain.EventLog;
import com.lloll.myro.domain.notification.domain.Notification;
import com.lloll.myro.domain.schedule.domain.Schedule;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String nickname;

    private String phone;
    private Date createdAt;
    private Date updatedAt;

    @OneToMany
    private List<Schedule> schedules;

    @OneToMany
    private List<Notification> notifications;

    @OneToMany
    private List<EventLog> logs;

}
