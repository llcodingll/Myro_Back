package com.lloll.myro.domain.schedule.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public enum ScheduleStatus {
    DELETED(0), //삭제된 일정(for soft delete)
    PENDING(1), //아직 시작되지 않음
    ACTIVE(2), //진행 중
    COMPLETE(3), //일정 종료
    CANCELLED(4), //사용자의 일정 취소 || 비활성화
    ON_HOLD(5), //미뤄진 일정
    RESCHEDULED(6), //일정 재조정
    EXPIRED(7); //유효기간이 지나 자동 무효된 일정

    private final int statusCode;

    ScheduleStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    private static final Map<Integer, ScheduleStatus> CODE_MAP =
            Arrays.stream(ScheduleStatus.values())
                    .collect(Collectors.toMap(ScheduleStatus::getStatusCode, Function.identity()));

    public static ScheduleStatus fromStatusCode(int statusCode) {
        ScheduleStatus status = CODE_MAP.get(statusCode);
        if (status == null) {
            throw new IllegalArgumentException("Invalid statusCode: " + statusCode);
        }
        return status;
    }
}
