package com.lloll.myro.domain.schedule.application;

import com.lloll.myro.domain.schedule.dao.ScheduleRepository;
import com.lloll.myro.domain.schedule.domain.Schedule;
import com.lloll.myro.domain.schedule.dto.ScheduleDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService{
    private final ScheduleRepository repository;

    @Override
    public ScheduleDto createSchedule(ScheduleDto request) {
        Schedule schedule = Schedule.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .isRecurring(request.isRecurring())
                .scheduleStatus(request.getScheduleStatus())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
        repository.save(request);
        return request;
    }

    @Override
    public List<ScheduleDto> getAllSchedules(ScheduleDto getScheduleDto) {
        return repository.findAllSchedule();
    }

    @Override
    public ScheduleDto getScheduleById(Long scheduleId) {
        return repository.findByScheduleId(scheduleId);
    }

    @Override
    public ScheduleDto updateSchedule(Long scheduleId, ScheduleDto updateScheduleDto) {
        return repository.updateByScheduleId(updateScheduleDto, scheduleId);
    }

    @Override
    public void deleteSchedule(Long scheduleId) {
        repository.deleteByScheduleId(scheduleId);
    }
}
