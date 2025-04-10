package com.lloll.myro.domain.schedule.domain;

import com.lloll.myro.domain.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scheduleId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User userId;

    private String title;
    private String description;

    private Boolean isRecurring; //반복 일정 여부(선택)
    private String recurrenceRule; //반복 일정 규칙(ex: "FREQ=WEEKLY;BYDAY=MO,WE,FR")

    @Enumerated(EnumType.STRING)
    private ScheduleStatus scheduleStatus;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Builder
    public Schedule(String title, String description, LocalDateTime startDate, LocalDateTime endDate, Boolean isRecurring, String recurrenceRule, ScheduleStatus scheduleStatus, User userId) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isRecurring = isRecurring;
        this.recurrenceRule = recurrenceRule;
        this.scheduleStatus = scheduleStatus;
        this.userId = userId;
    }
}
