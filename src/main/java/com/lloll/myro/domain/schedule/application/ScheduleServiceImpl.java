package com.lloll.myro.domain.schedule.application;

import com.lloll.myro.domain.schedule.dao.ScheduleRepository;
import com.lloll.myro.domain.schedule.dao.TagRepository;
import com.lloll.myro.domain.schedule.domain.RecurrenceRule;
import com.lloll.myro.domain.schedule.domain.Schedule;
import com.lloll.myro.domain.schedule.domain.ScheduleStatus;
import com.lloll.myro.domain.schedule.domain.Tag;
import com.lloll.myro.domain.schedule.api.request.ScheduleDto;
import com.lloll.myro.domain.schedule.application.response.ScheduleResponseDto;
import com.lloll.myro.domain.schedule.api.request.UpdateScheduleDto;
import com.lloll.myro.domain.schedule.mapper.ScheduleMapper;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository repository;
    private final TagRepository tagRepository;
    private final ScheduleMapper mapper;

    @Override
    @Transactional
    public void createSchedule(ScheduleDto createRequest) {
        validateTimeConflict(createRequest.getStartDate(), createRequest.getEndDate());

        ScheduleStatus status = resolveScheduleStatus(createRequest);
        Schedule schedule = mapper.toEntity(createRequest, status);

        if (schedule.getRecurrenceRule() != null && schedule.getRecurrenceRule() == RecurrenceRule.CUSTOM) {
            applyCustomRecurrenceRule(schedule, createRequest.getCustomRecurrenceRule());
        }

        applyTagsToSchedule(schedule, createRequest.getTagNames());

        repository.save(schedule);
    }

    private void applyTagsToSchedule(Schedule schedule, List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) return;

        for (String name : tagNames) {
            Tag tag = tagRepository.findByName(name)
                    .orElseGet(() -> tagRepository.save(new Tag(name)));
            schedule.addTag(tag);
        }
    }

    private void validateTimeConflict(LocalDateTime startTime, LocalDateTime endTime) {
        if (isAllDaySchedule(startTime, endTime)) {
            return;
        }

        List<Schedule> sameDateSchedules = repository.findTimeConflictsOnly(startTime, endTime);

        boolean hasTimeConflict = sameDateSchedules.stream()
                .filter(this::hasTimeInfo)
                .anyMatch(s -> isTimeOverlapping(startTime.toLocalTime(), endTime.toLocalTime(),
                        s.getStartDate().toLocalTime(), s.getEndDate().toLocalTime()));

        if (hasTimeConflict) {
            throw new IllegalArgumentException("A time conflict exists with another schedule on the same time.");
        }
    }

    private boolean isAllDaySchedule(LocalDateTime start, LocalDateTime end) {
        return start.toLocalTime().equals(LocalTime.MIN) && end.toLocalTime().equals(LocalTime.MIN);
    }

    private boolean hasTimeInfo(Schedule schedule) {
        return !schedule.getStartDate().toLocalTime().equals(LocalTime.MIN)
                || !schedule.getEndDate().toLocalTime().equals(LocalTime.MIN);
    }

    private boolean isTimeOverlapping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return !start1.isAfter(end2) && !end1.isBefore(start2);
    }

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
