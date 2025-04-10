package com.lloll.myro.domain.schedule.domain;

public enum ScheduleStatus {
    PENDING, //아직 시작되지 않음
    ACTIVE, //진행 중
    COMPLETE, //일정 종료
    CANCELLED, //사용자의 일정 취소 || 비활성화
    ON_HOLD, //미뤄진 일정
    DELETED, //(soft delete)
    RESCHEDULED, //일정 재조정
    EXPIRED; //유효기간이 지나 자동 무효된 일정
}
