package com.lloll.myro.domain.schedule.application;

import com.lloll.myro.domain.schedule.dao.ScheduleRepository;
import com.lloll.myro.domain.schedule.domain.Schedule;
import com.lloll.myro.domain.schedule.domain.ScheduleStatus;
import com.lloll.myro.domain.schedule.dto.ScheduleDto;
import com.lloll.myro.domain.schedule.dto.UpdateScheduleDto;
import com.lloll.myro.domain.schedule.mapper.ScheduleMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService{
    private final ScheduleRepository repository;
    private final ScheduleMapper mapper;

    @Override
    @Transactional
    public void createSchedule(ScheduleDto createRequest) {
        validateTimeConflict(createRequest.getStartDate(), createRequest.getEndDate());

        ScheduleStatus status = resolveScheduleStatus(createRequest);
        Schedule schedule = mapper.toEntity(createRequest, status);

        repository.save(schedule);
    }
    private void validateTimeConflict(LocalDateTime startTime, LocalDateTime endTime) {
        List<Schedule> conflicts = repository.findConflictingSchedules(startTime, endTime);
        if (!conflicts.isEmpty()) {
            throw new IllegalArgumentException("A time conflict exists with another schedule on the same date.");
        }
    }
    private ScheduleStatus resolveScheduleStatus(ScheduleDto dto) {
        return dto.getScheduleStatus() != null
                ? dto.getScheduleStatus()
                : autoStatus(dto.getStartDate(), dto.getEndDate());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleDto> getAllSchedules() {
        return repository.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ScheduleDto getScheduleById(Long scheduleId) {
        Schedule schedule = repository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        return mapper.toResponse(schedule);
    }

    @Override
    @Transactional
    public void updateSchedule(Long scheduleId, UpdateScheduleDto updateRequest) {
        Schedule schedule = repository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        mapper.updateEntity(schedule, updateRequest);
    }

    @Override
    @Transactional
    public void deleteSchedule(Long scheduleId) {
        Schedule schedule = repository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        schedule.isDeleted();

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
