package com.lloll.myro.domain.schedule.mapper;

import com.lloll.myro.domain.schedule.domain.Schedule;
import com.lloll.myro.domain.schedule.dto.ScheduleDto;

public class ScheduleMapper {
    public Schedule toEntity(ScheduleDto dto) {
        return Schedule.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .isRecurring(dto.isRecurring())
                .recurrenceRule(dto.getRecurrenceRule())
                .scheduleStatus(dto.getScheduleStatus())
                .build();
    }

    public ScheduleDto toDto(Schedule schedule) {
        return ScheduleDto.builder()
                .title(schedule.getTitle())
                .description(schedule.getDescription())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .isRecurring(schedule.isRecurring())
                .recurrenceRule(schedule.getRecurrenceRule())
                .scheduleStatus(schedule.getScheduleStatus())
                .build();
    }
}
