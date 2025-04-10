package com.lloll.myro.domain.schedule.dto;

import com.lloll.myro.domain.user.domain.User;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScheduleDto {
    @NotNull
    private User userId;
    @NotNull
    private String title;
    @NotNull
    private String description;

    private boolean isRecurring;
    private String recurrenceRule;
    private String scheduleStatus;

    private Date startDate;
    private Date endDate;

    @Builder
    public ScheduleDto(String title, String description, Date startDate, Date endDate, boolean isRecurring, String recurrenceRule, String scheduleStatus, User userId) {
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
