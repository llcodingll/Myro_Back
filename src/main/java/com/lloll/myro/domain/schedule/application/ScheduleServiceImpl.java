package com.lloll.myro.domain.schedule.application;

import com.lloll.myro.domain.schedule.dao.ScheduleRepository;
import com.lloll.myro.domain.schedule.dto.ScheduleDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService{
    private final ScheduleRepository repository;

    @Override
    public void createSchedule(ScheduleDto createScheduleDto) {
        ScheduleDto scheduleDto = ScheduleDto.builder()
                .title(createScheduleDto.getTitle())
                .description(createScheduleDto.getDescription())
                .build();
        repository.save(createScheduleDto);
    }

    @Override
    public List<ScheduleDto> getAllSchedules(ScheduleDto getScheduleDto) {
        return repository.findAllSchedule();
    }

    @Override
    public ScheduleDto getScheduleById(ScheduleDto getScheduleDto, Long scheduleId) {
        return repository.findByScheduleId(scheduleId);
    }

    @Override
    public ScheduleDto updateSchedule(ScheduleDto updateScheduleDto, Long scheduleId) {
        return repository.updateByScheduleId(updateScheduleDto, scheduleId);
    }

    @Override
    public void deleteSchedule(Long scheduleId) {
        repository.deleteByScheduleId(scheduleId);
    }
}
