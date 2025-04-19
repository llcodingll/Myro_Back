package com.lloll.myro.domain.schedule.application;

import com.lloll.myro.domain.schedule.dto.ScheduleDto;
import com.lloll.myro.domain.schedule.dto.ScheduleResponseDto;
import com.lloll.myro.domain.schedule.dto.UpdateScheduleDto;
import java.util.List;

public interface ScheduleService {

    void createSchedule(ScheduleDto createScheduleDto);
    List<ScheduleResponseDto> getAllSchedules();
    ScheduleResponseDto getScheduleById(Long scheduleId);
    ScheduleResponseDto updateSchedule(Long scheduleId, UpdateScheduleDto updateScheduleDto);
    void deleteSchedule(Long scheduleId);
}
