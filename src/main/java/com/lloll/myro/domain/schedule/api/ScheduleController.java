package com.lloll.myro.domain.schedule.api;

import com.lloll.myro.domain.schedule.application.ScheduleService;
import com.lloll.myro.domain.schedule.dto.ScheduleDto;
import com.lloll.myro.domain.schedule.dto.UpdateScheduleDto;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService service;

    @PostMapping()
    public void createSchedule(@Valid @RequestBody ScheduleDto request) {
        service.createSchedule(request);
    }

    @GetMapping()
    public List<ScheduleDto> searchSchedules() {
        return service.getAllSchedules();
    }

    @GetMapping("/{scheduleId}")
    public ScheduleDto searchScheduleById(@PathVariable Long scheduleId) {
        return service.getScheduleById(scheduleId);
    }

    @PostMapping("/{scheduleId}")
    public void updateScheduleById(@PathVariable Long scheduleId, @Valid @RequestBody UpdateScheduleDto updateRequest) {
        service.updateSchedule(scheduleId, updateRequest);
    }

    @DeleteMapping("/{scheduleId}")
    public void deleteScheduleById(@PathVariable Long scheduleId) {
        service.deleteSchedule(scheduleId);
    }
}
