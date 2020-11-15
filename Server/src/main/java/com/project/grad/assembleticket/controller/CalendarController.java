package com.project.grad.assembleticket.controller;

import com.project.grad.assembleticket.domain.entity.Calendar;
import com.project.grad.assembleticket.dto.CalendarSaveRequestDto;
import com.project.grad.assembleticket.dto.CalendarUpdateRequestDto;
import com.project.grad.assembleticket.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/assemble-ticket")
@RestController
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    // 해당 User 해당 Date의 일정 가져오기
    @GetMapping("/calendar")
    public List<Calendar> getCalendar(@RequestParam String email, @RequestParam String date) {
        return calendarService.getCalendar(email, LocalDate.parse(date));
    }

    // 일정 등록하기
    @PostMapping("/calendar")
    public Calendar saveCalendar(@RequestBody CalendarSaveRequestDto requestDto){
        return calendarService.saveCalendar(requestDto);
    }

    // 일정 수정하기
    @PutMapping("/calendar")
    public Calendar updateCalendar(@RequestParam Long calId, @RequestBody CalendarUpdateRequestDto requestDto){
        return calendarService.updateCalendar(calId, requestDto);
    }

    // 일정 삭제하기
    @DeleteMapping("/calendar")
    public Long deleteCalendar(@RequestParam Long calId){
        return calendarService.deleteCalendar(calId);
    }

}
