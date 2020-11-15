package com.project.grad.assembleticket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class CalendarSaveRequestDto {

    private String email;
    private Long showId;
    private LocalDate calDate;
    private LocalTime calTime;
    private String calTitle;
    private String calMemo;
    private int alarmSet;

    @Builder
    public CalendarSaveRequestDto(String email, Long show, String date, String time, String title, String memo, int alarmSet){
        this.email = email;
        this.showId = show;
        this.calDate = LocalDate.parse(date);
        this.calTime = LocalTime.parse(time);
        this.calTitle = title;
        this.calMemo = memo;
        this.alarmSet = alarmSet;
    }

}
