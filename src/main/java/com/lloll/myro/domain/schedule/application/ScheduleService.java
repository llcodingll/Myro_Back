package com.lloll.myro.domain.schedule.application;

import com.lloll.myro.domain.schedule.api.request.ScheduleDto;
import com.lloll.myro.domain.schedule.application.response.ScheduleResponseDto;
import com.lloll.myro.domain.schedule.api.request.UpdateScheduleDto;
import java.util.List;

public interface ScheduleService {

    void createSchedule(ScheduleDto createScheduleDto);
    List<ScheduleResponseDto> getAllSchedules();
    ScheduleResponseDto getScheduleById(Long scheduleId);
    ScheduleResponseDto updateSchedule(Long scheduleId, UpdateScheduleDto updateScheduleDto);
    void deleteSchedule(Long scheduleId);
}
