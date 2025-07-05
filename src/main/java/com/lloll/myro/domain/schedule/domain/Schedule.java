package com.lloll.myro.domain.schedule.domain;

import com.lloll.myro.domain.schedule.convert.ScheduleStatusConverter;
import com.lloll.myro.domain.account.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "schedules")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;
    private String description;

    private Boolean isRecurring = false; //반복 일정 여부(선택)
    @Enumerated(EnumType.STRING)
    private RecurrenceRule recurrenceRule; //반복 일정 규칙(ex: "FREQ=WEEKLY;BYDAY=MO,WE,FR")
    private String customRecurrenceRule; //반복 일정 사용자 커스텀
    private LocalDateTime startRecurrenceDate; //반복 일정 시작
    private LocalDateTime endRecurrenceDate; //반복 일정 끝

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<ScheduleTag> scheduleTags = new ArrayList<>();
    public List<String> getTagNames() {
        return this.scheduleTags.stream()
                .map(scheduleTag -> scheduleTag.getTag().getName())
                .collect(Collectors.toList());
    }


    @Column(name = "schedule_status")
    @Convert(converter = ScheduleStatusConverter.class)
    private ScheduleStatus scheduleStatus;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Builder
    public Schedule(String title, String description, LocalDateTime startDate, LocalDateTime endDate, Boolean isRecurring, RecurrenceRule recurrenceRule, String customRecurrenceRule, LocalDateTime startRecurrenceDate, LocalDateTime endRecurrenceDate, ScheduleStatus scheduleStatus, User user) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isRecurring = isRecurring != null ? isRecurring : false;;
        this.recurrenceRule = recurrenceRule;
        this.customRecurrenceRule = customRecurrenceRule;
        this.startRecurrenceDate = startRecurrenceDate;
        this.endRecurrenceDate = endRecurrenceDate;
        this.scheduleStatus = scheduleStatus;
        this.user = user;
    }

    public void addTag(Tag tag) {
        ScheduleTag scheduleTag = new ScheduleTag(this, tag);
        this.scheduleTags.add(scheduleTag);
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void changeStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void changeEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void changeIsRecurring(Boolean isRecurring) {
        this.isRecurring = isRecurring;
    }

    public void changeRecurrenceRule(RecurrenceRule recurrenceRule) {
        this.recurrenceRule = recurrenceRule;
    }

    public void changeCustomRecurrenceRule(String customRecurrenceRule) {
        this.customRecurrenceRule = customRecurrenceRule;
    }

    public void changeStartRecurrenceDate(LocalDateTime startRecurrenceDate) {
        this.startRecurrenceDate = startRecurrenceDate;
    }

    public void changeEndRecurrenceDate(LocalDateTime endRecurrenceDate) {
        this.endRecurrenceDate = endRecurrenceDate;
    }

    public void changeScheduleStatus(ScheduleStatus scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    public void isDeleted() {
        this.scheduleStatus = ScheduleStatus.DELETED;
    }

}
