package com.lloll.myro.domain.schedule.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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
}
