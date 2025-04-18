package com.lloll.myro.domain.schedule.application;

import com.lloll.myro.domain.schedule.dao.ScheduleRepository;
import com.lloll.myro.domain.schedule.domain.RecurrenceRule;
import com.lloll.myro.domain.schedule.domain.Schedule;
import com.lloll.myro.domain.schedule.domain.ScheduleStatus;
import com.lloll.myro.domain.schedule.dto.ScheduleDto;
import com.lloll.myro.domain.schedule.dto.ScheduleResponseDto;
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
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository repository;
    private final ScheduleMapper mapper;

    @Override
    @Transactional
    public void createSchedule(ScheduleDto createRequest) {
//        validateTimeConflict(createRequest.getStartDate(), createRequest.getEndDate());

        ScheduleStatus status = resolveScheduleStatus(createRequest);
        Schedule schedule = mapper.toEntity(createRequest, status);

        if (schedule.getRecurrenceRule() == RecurrenceRule.CUSTOM) {
            applyCustomRecurrenceRule(schedule, createRequest.getCustomRecurrenceRule());
        }

        repository.save(schedule);
    }
    //이거 같은 날짜 내 같은 시간으로 바꿔야 함 => 현재 요청 시 같은 날짜에 등록이 안 되는 문제 발생
    //같은 날짜 내에서 같은 시간에 해당하면 등록이 안 되도록 수정 필요
//    private void validateTimeConflict(LocalDateTime startTime, LocalDateTime endTime) {
//        List<Schedule> conflicts = repository.findConflictingSchedules(startTime, endTime);
//        if (!conflicts.isEmpty()) {
//            throw new IllegalArgumentException("A time conflict exists with another schedule on the same date.");
//        }
//    }
    private ScheduleStatus resolveScheduleStatus(ScheduleDto dto) {
        return dto.getScheduleStatus() != null
                ? dto.getScheduleStatus()
                : autoStatus(dto.getStartDate(), dto.getEndDate());
    }
    private void applyCustomRecurrenceRule(Schedule schedule, String customRecurrenceRule) {
        if (customRecurrenceRule == null || customRecurrenceRule.isBlank()) {
            throw new IllegalArgumentException("Custom recurrence rule must be provided for CUSTOM type.");
        }

        schedule.changeCustomRecurrenceRule(customRecurrenceRule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> getAllSchedules() {
        return repository.findByScheduleStatusNot(ScheduleStatus.DELETED).stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ScheduleResponseDto getScheduleById(Long scheduleId) {
        Schedule schedule = repository.findByIdAndScheduleStatusNot(scheduleId, ScheduleStatus.DELETED)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        return mapper.toResponse(schedule);
    }

    @Override
    @Transactional
    public ScheduleResponseDto updateSchedule(Long scheduleId, UpdateScheduleDto updateRequest) {
        Schedule schedule = repository.findByIdAndScheduleStatusNot(scheduleId, ScheduleStatus.DELETED)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        return mapper.updateEntity(schedule, updateRequest);
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
        if(startDate != null && endDate != null) {
            if(now.isBefore(startDate)) return ScheduleStatus.PENDING;
            if((now.isEqual(startDate) || now.isAfter(startDate)) && (now.isEqual(endDate) || now.isBefore(endDate))) return ScheduleStatus.ACTIVE;
            if(now.isAfter(endDate)) return ScheduleStatus.COMPLETE;
            return ScheduleStatus.COMPLETE;
        }
        return ScheduleStatus.PENDING;
    }
}
