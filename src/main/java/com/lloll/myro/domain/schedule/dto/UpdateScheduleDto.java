package com.lloll.myro.domain.schedule.dto;

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
    private String recurrenceRule;
    private ScheduleStatus scheduleStatus;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Builder
    public UpdateScheduleDto(Long scheduleId, String title, String description, Boolean isRecurring, String recurrenceRule, ScheduleStatus scheduleStatus, LocalDateTime startDate, LocalDateTime endDate) {
        this.scheduleId = scheduleId;
        this.title = title;
        this.description = description;
        this.isRecurring = isRecurring;
        this.recurrenceRule = recurrenceRule;
        this.scheduleStatus = scheduleStatus;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
