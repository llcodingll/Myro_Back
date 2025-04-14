package com.lloll.myro.domain.schedule.dto;

import com.lloll.myro.domain.schedule.domain.ScheduleStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScheduleDto {
    @NotNull
    private String title;
    @NotNull
    private String description;

    private Boolean isRecurring;
    private String recurrenceRule;
    private ScheduleStatus scheduleStatus;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Builder
    public ScheduleDto(String title, String description, LocalDateTime startDate, LocalDateTime endDate, Boolean isRecurring, String recurrenceRule, ScheduleStatus scheduleStatus) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isRecurring = isRecurring;
        this.recurrenceRule = recurrenceRule;
        this.scheduleStatus = scheduleStatus;
    }
}
