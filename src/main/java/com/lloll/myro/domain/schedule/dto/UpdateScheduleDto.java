package com.lloll.myro.domain.schedule.dto;

import com.lloll.myro.domain.schedule.domain.ScheduleStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateScheduleDto {
    @NotNull
    private Long ScheduleId;
    @NotNull
    private String title;
    @NotNull
    private String description;

    private Boolean isRecurring;
    private String recurrenceRule;
    private ScheduleStatus scheduleStatus;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
