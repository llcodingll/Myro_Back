package com.lloll.myro.domain.schedule.domain;

import lombok.Getter;

@Getter
public enum RecurrenceRule {
    NONE,
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY,
    CUSTOM
}