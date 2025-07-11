package com.lloll.myro.domain.schedule.api.request;

import com.lloll.myro.domain.schedule.domain.RecurrenceRule;
import com.lloll.myro.domain.schedule.domain.ScheduleStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateScheduleDto {

    private Long scheduleId;
    private String title;
    private String description;

    private Boolean isRecurring;
    private RecurrenceRule recurrenceRule;
    private String customRecurrenceRule;
    private LocalDateTime startRecurrenceDate;
    private LocalDateTime endRecurrenceDate;

    private ScheduleStatus scheduleStatus;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Builder
    public UpdateScheduleDto(Long scheduleId, String title, String description, Boolean isRecurring, RecurrenceRule recurrenceRule, String customRecurrenceRule, LocalDateTime startRecurrenceDate, LocalDateTime endRecurrenceDate, ScheduleStatus scheduleStatus, LocalDateTime startDate, LocalDateTime endDate) {
        this.scheduleId = scheduleId;
        this.title = title;
        this.description = description;
        this.isRecurring = isRecurring;
        this.recurrenceRule = recurrenceRule;
        this.customRecurrenceRule = customRecurrenceRule;
        this.startRecurrenceDate = startRecurrenceDate;
        this.endRecurrenceDate = endRecurrenceDate;
        this.scheduleStatus = scheduleStatus;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
