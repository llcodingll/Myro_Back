package com.lloll.myro.domain.schedule.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class ScheduleDto {
    @NotNull
    private String title;
    @NotNull
    private String description;

    private boolean isRecurring;
    private String recurrenceRule;
    private String scheduleStatus;

    private Date startDate;
    private Date endDate;

    public ScheduleDto(String title, String description, boolean isRecurring, String recurrenceRule,
                       String scheduleStatus, Date startDate, Date endDate) {
        this.title = title;
        this.description = description;
        this.isRecurring = isRecurring;
        this.recurrenceRule = recurrenceRule;
        this.scheduleStatus = scheduleStatus;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
