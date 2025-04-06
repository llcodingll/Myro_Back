package com.lloll.myro.domain.schedule.api;

import com.lloll.myro.domain.schedule.application.ScheduleService;
import com.lloll.myro.domain.schedule.dto.ScheduleDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService service;

    @PostMapping("/id")
    public void createSchedule(@RequestBody ScheduleDto createScheduleDto) {
        service.createSchedule(createScheduleDto);
    }

    @GetMapping()
    public List<ScheduleDto> searchSchedules(ScheduleDto searchScheduleDto) {
        return service.getAllSchedules(searchScheduleDto);
    }

    @GetMapping("/id")
    public ScheduleDto searchScheduleById(ScheduleDto searchScheduleDto, Long scheduleId) {
        return service.getScheduleById(searchScheduleDto, scheduleId);
    }

    @PostMapping("/id")
    public void updateScheduleById(ScheduleDto updateScheduleDto, Long scheduleId) {
        service.updateSchedule(updateScheduleDto, scheduleId);
    }

    @DeleteMapping("/id")
    public void deleteScheduleById(Long scheduleId) {
        service.deleteSchedule(scheduleId);
    }
}
