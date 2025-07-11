package com.lloll.myro.domain.schedule.mapper;

import com.lloll.myro.domain.schedule.domain.Schedule;
import com.lloll.myro.domain.schedule.domain.ScheduleStatus;
import com.lloll.myro.domain.schedule.api.request.ScheduleDto;
import com.lloll.myro.domain.schedule.application.response.ScheduleResponseDto;
import com.lloll.myro.domain.schedule.api.request.UpdateScheduleDto;
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

    public ScheduleResponseDto toResponse(Schedule schedule) {
        return ScheduleResponseDto.builder()
                .scheduleId(schedule.getId())
                .title(schedule.getTitle())
                .description(schedule.getDescription())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .isRecurring(schedule.getIsRecurring())
                .recurrenceRule(schedule.getRecurrenceRule())
                .scheduleStatus(schedule.getScheduleStatus())
                .tagNames(schedule.getTagNames())
                .build();
    }

    public ScheduleResponseDto updateEntity(Schedule schedule, UpdateScheduleDto dto) {
        if (dto.getTitle() != null) {
            schedule.changeTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            schedule.changeDescription(dto.getDescription());
        }
        if (dto.getStartDate() != null) {
            schedule.changeStartDate(dto.getStartDate());
        }
        if (dto.getEndDate() != null) {
            schedule.changeEndDate(dto.getEndDate());
        }
        if (dto.getIsRecurring() != null) {
            schedule.changeIsRecurring(dto.getIsRecurring());
        }
        if (dto.getRecurrenceRule() != null) {
            schedule.changeRecurrenceRule(dto.getRecurrenceRule());
        }
        if(dto.getCustomRecurrenceRule() != null) {
            schedule.changeCustomRecurrenceRule(dto.getCustomRecurrenceRule());
        }
        if(dto.getStartRecurrenceDate() != null) {
            schedule.changeStartRecurrenceDate(dto.getStartRecurrenceDate());
        }
        if(dto.getEndRecurrenceDate() != null) {
            schedule.changeEndRecurrenceDate(dto.getEndRecurrenceDate());
        }
        if (dto.getScheduleStatus() != null) {
            schedule.changeScheduleStatus(dto.getScheduleStatus());
        }
        return toResponse(schedule);
    }
}
