package com.lloll.myro.domain.schedule.convert;

import com.lloll.myro.domain.schedule.domain.ScheduleStatus;
import jakarta.persistence.AttributeConverter;

public class ScheduleStatusConverter implements AttributeConverter<ScheduleStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ScheduleStatus status) {
        return status != null ? status.getStatusCode() : null;
    }

    @Override
    public ScheduleStatus convertToEntityAttribute(Integer dbData) {
        return dbData != null ? ScheduleStatus.fromStatusCode(dbData) : null;
    }
}
