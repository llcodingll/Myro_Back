package com.lloll.myro.domain.schedule.mapper;

import com.lloll.myro.domain.schedule.domain.Schedule;
import com.lloll.myro.domain.schedule.domain.ScheduleStatus;
import com.lloll.myro.domain.schedule.dto.ScheduleDto;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class ScheduleMapper {
    public Schedule toEntity(ScheduleDto dto, ScheduleStatus scheduleStatus) {
        return Schedule.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .isRecurring(dto.getIsRecurring())
                .recurrenceRule(dto.getRecurrenceRule())
                .scheduleStatus(scheduleStatus)
                .build();
    }

    public ScheduleDto toResponse(Schedule schedule) {
        return ScheduleDto.builder()
                .title(schedule.getTitle())
                .description(schedule.getDescription())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .isRecurring(schedule.getIsRecurring())
                .recurrenceRule(schedule.getRecurrenceRule())
                .scheduleStatus(schedule.getScheduleStatus())
                .build();
    }

    public void updateEntity(Schedule schedule, ScheduleDto dto, ScheduleStatus status) {
        Optional.ofNullable(dto.getTitle()).ifPresent(schedule::changeTitle);
        Optional.ofNullable(dto.getDescription()).ifPresent(schedule::changeDescription);
        Optional.ofNullable(dto.getStartDate()).ifPresent(schedule::changeStartDate);
        Optional.ofNullable(dto.getEndDate()).ifPresent(schedule::changeEndDate);
        Optional.ofNullable(dto.getIsRecurring()).ifPresent(schedule::changeRecurring);
        Optional.ofNullable(dto.getRecurrenceRule()).ifPresent(schedule::changeRecurrenceRule);
        schedule.changeStatus(status);
    }

}
