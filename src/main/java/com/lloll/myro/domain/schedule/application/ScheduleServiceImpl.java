package com.lloll.myro.domain.schedule.application;

import com.lloll.myro.domain.schedule.dao.ScheduleRepository;
import com.lloll.myro.domain.schedule.domain.Schedule;
import com.lloll.myro.domain.schedule.domain.ScheduleStatus;
import com.lloll.myro.domain.schedule.dto.ScheduleDto;
import com.lloll.myro.domain.schedule.mapper.ScheduleMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService{
    private final ScheduleRepository repository;
    private final ScheduleMapper mapper;

    @Override
    public void createSchedule(ScheduleDto createRequest) {
        ScheduleStatus status = createRequest.getScheduleStatus();
        if(status == null){
            status = autoStatus(createRequest.getStartDate(), createRequest.getEndDate());
        }
        Schedule schedule = mapper.toEntity(createRequest, status);
        repository.save(schedule);
    }

    @Override
    public List<ScheduleDto> getAllSchedules(ScheduleDto getAllRequest) {
        return repository.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public ScheduleDto getScheduleById(Long scheduleId) {
        Schedule schedule = repository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        return mapper.toResponse(schedule);
    }

    @Override
    public ScheduleDto updateSchedule(Long scheduleId, ScheduleDto updateRequest) {
        Schedule schedule = repository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        updater.update(schedule, updateRequest);
        return mapper.toResponse(schedule);
    }

    @Override
    public void deleteSchedule(Long scheduleId) {
        repository.deleteById(scheduleId);
    }

    private ScheduleStatus autoStatus(LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime now = LocalDateTime.now();
        if(startDate != null && endDate != null){
            if(now.isBefore(startDate)) return ScheduleStatus.PENDING;
            if(now.isAfter(endDate)) return ScheduleStatus.ACTIVE;
            return ScheduleStatus.COMPLETE;
        }
        return ScheduleStatus.PENDING;
    }
}
