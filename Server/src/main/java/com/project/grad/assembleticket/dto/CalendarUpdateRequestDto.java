package com.project.grad.assembleticket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class CalendarUpdateRequestDto {

    private Long calId;
    private LocalDate calDate;
    private LocalTime calTime;
    private String calTitle;
    private String calMemo;
    private int alarmSet;

    @Builder
    public CalendarUpdateRequestDto(Long calId, LocalDate date, LocalTime time, String title, String memo, int alarmSet){
        this.calId = calId;
        this.calDate = date;
        this.calTime = time;
        this.calTitle = title;
        this.calMemo = memo;
        this.alarmSet = alarmSet;
    }

}
