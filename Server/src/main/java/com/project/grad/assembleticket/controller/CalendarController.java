package com.project.grad.assembleticket.controller;

import com.project.grad.assembleticket.domain.entity.Calendar;
import com.project.grad.assembleticket.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/assemble-ticket")
@RestController
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    // 해당 User 해당 Date의 일정 가져오기
    @GetMapping("/calendar")
    public List<Calendar> getCalendar(@RequestParam Long userId, @RequestParam String date) {
        return calendarService.getCalendar(userId, LocalDate.parse(date));
    }

}
