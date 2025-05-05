package com.lloll.myro.domain.schedule.dto;

import com.lloll.myro.domain.schedule.domain.RecurrenceRule;
import com.lloll.myro.domain.schedule.domain.ScheduleStatus;
import com.lloll.myro.domain.user.domain.User;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScheduleDto {
    private User userId; //반복 일정 관련 에러 빙지를 위해 임의로 추가한 코드(수정 필요!!)
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

    private List<String> tagNames;

    @Builder
    public ScheduleDto(String title, String description, LocalDateTime startDate, LocalDateTime endDate, Boolean isRecurring, RecurrenceRule recurrenceRule, String customRecurrenceRule, LocalDateTime startRecurrenceDate, LocalDateTime endRecurrenceDate, ScheduleStatus scheduleStatus, List<String> tagNames) {
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
        this.tagNames = tagNames;
    }
}
