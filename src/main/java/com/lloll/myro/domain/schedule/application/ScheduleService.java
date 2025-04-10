package com.lloll.myro.domain.schedule.application;

import com.lloll.myro.domain.schedule.dto.ScheduleDto;
import java.util.List;

public interface ScheduleService {

    ScheduleDto createSchedule(ScheduleDto createScheduleDto);
    List<ScheduleDto> getAllSchedules(ScheduleDto getScheduleDto);
    ScheduleDto getScheduleById(Long scheduleId);
    ScheduleDto updateSchedule(Long scheduleId, ScheduleDto updateScheduleDto);
    void deleteSchedule(Long scheduleId);
}
