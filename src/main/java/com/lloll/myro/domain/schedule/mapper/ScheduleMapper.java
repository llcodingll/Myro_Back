package com.lloll.myro.domain.schedule.mapper;

import com.lloll.myro.domain.schedule.domain.Schedule;
import com.lloll.myro.domain.schedule.domain.ScheduleStatus;
import com.lloll.myro.domain.schedule.dto.ScheduleDto;
import com.lloll.myro.domain.schedule.dto.UpdateScheduleDto;
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

    public void updateEntity(Schedule schedule, UpdateScheduleDto dto) {
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
        if (dto.getScheduleStatus() != null) {
            schedule.changeScheduleStatus(dto.getScheduleStatus());
        }
    }

}
