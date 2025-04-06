package com.lloll.myro.domain.schedule.application;

import com.lloll.myro.domain.schedule.dto.ScheduleDto;
import java.util.List;

public interface ScheduleService {

    void createSchedule(ScheduleDto createScheduleDto);
    List<ScheduleDto> getAllSchedules(ScheduleDto getScheduleDto);
    ScheduleDto getScheduleById(ScheduleDto getScheduleDto, Long scheduleId);
    ScheduleDto updateSchedule(ScheduleDto updateScheduleDto, Long scheduleId);
    void deleteSchedule(Long scheduleId);
}
