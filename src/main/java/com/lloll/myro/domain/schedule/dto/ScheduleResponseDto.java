package com.lloll.myro.domain.schedule.dto;

import com.lloll.myro.domain.schedule.domain.RecurrenceRule;
import com.lloll.myro.domain.schedule.domain.ScheduleStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScheduleResponseDto {
    @NotNull
    private Long scheduleId;
    @NotNull
    private String title;
    @NotNull
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
    public ScheduleResponseDto(Long scheduleId, String title, String description, LocalDateTime startDate,
                               LocalDateTime endDate, Boolean isRecurring, RecurrenceRule recurrenceRule, String customRecurrenceRule, LocalDateTime startRecurrenceDate, LocalDateTime endRecurrenceDate, ScheduleStatus scheduleStatus) {
        this.scheduleId = scheduleId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isRecurring = isRecurring;
        this.recurrenceRule = recurrenceRule;
        this.customRecurrenceRule = customRecurrenceRule;
        this.startRecurrenceDate = startRecurrenceDate;
        this.endRecurrenceDate = endRecurrenceDate;
        this.scheduleStatus = scheduleStatus;
    }
}
