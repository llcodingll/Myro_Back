package com.lloll.myro.domain.schedule.dao;

import com.lloll.myro.domain.schedule.dto.ScheduleDto;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository {
    void save(ScheduleDto scheduleDto);
    List<ScheduleDto> findAllSchedule();
    ScheduleDto findByScheduleId(Long scheduleId);
    ScheduleDto updateByScheduleId(ScheduleDto updateScheduleDto, Long scheduleId);
    void deleteByScheduleId(Long scheduleId);
}
